package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.User;
import com.example.qlybanhang.config.RandomPassword;
import com.example.qlybanhang.repository.UserRepository;
import com.example.qlybanhang.config.Login;
import com.example.qlybanhang.service.MailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class LoginController {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final Login login;

    public LoginController(MailService mailService, UserRepository userRepository, Login login) {
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.login = login;
    }


    @GetMapping("/doLogin")
    public String loginPage() {
        return "accessAccount/login";
    }

    @PostMapping("/doLogin")
    public String login(HttpServletRequest request, @RequestParam(name = "e", required = false) String error,
                        HttpSession session) {
        if (error != null) {
            request.setAttribute("e", error);
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUsername(userDetails.getUsername());
        session.setAttribute("user", user);
        request.setAttribute("userDetail", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "content-page-client";
    }

    @GetMapping("/forgotPassword")
    public String getPassword() {
        return "accessAccount/RecoverPassword";
    }

    //Forgot password
    @PostMapping("/forgotPassword")
    public String getPassword(Model model, @RequestParam(name = "email", required = false) String email) {
        RandomPassword randomPassword = new RandomPassword();
        String newPassword = randomPassword.randomAlphaNumeric(8);
        AtomicBoolean mark = new AtomicBoolean(false);
        List<User> userList = userRepository.findAll();
        userList.forEach(item -> {
            if (item.getEmail().compareTo(email) == 0) {
                new Thread(() -> mailService.sendMail(email, "RecoverPassword", "Your new password : " + newPassword)).start();
                mark.set(true);
            }
        });
        if (!mark.get()) {
            model.addAttribute("message", "Email not exists");
            return "accessAccount/login";
        } else {
            User user = new User();
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
            return "accessAccount/Access";
        }
    }
}
