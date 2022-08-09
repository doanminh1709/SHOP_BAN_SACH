package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.User;
import com.example.qlybanhang.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //User or admin have see this information.
    @PostAuthorize("#returnObject.username  "
            + " == authentication.principal.username "
            + "or hasRole('ROLE_ADMIN')")
    @GetMapping("/detailUser/{id}")
    public String detailUser(Model model, @PathVariable(name = "id") int id) {
        model.addAttribute("detailUser", userRepository.getById(id));
        return "user/detail";
    }


    @GetMapping("/admin/listUser")
    public String getListUser(Model model) {
        model.addAttribute("listUser", userRepository.findAll());
        return "user/view";
    }

    @GetMapping("/addUser")
    public String createNewUser() {
        return "user/add.html";
    }

    @PostMapping("/addUser")
    public String createNewUser(@ModelAttribute User user) {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        user.setRole(roles);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/doLogin";
    }

    //edit
    @GetMapping("/user/editUser")
    public String editUser(@RequestParam(name = "id", required = false) int id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("editById", user);
        return "user/edit";
    }

    @PostMapping("/user/editUser")
    public String editUser(@ModelAttribute User user) {
        userRepository.save(user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return "redirect:/admin/listUser";
    }

    //delete user to database
    @GetMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam(name = "id", required = false) int id) {
        userRepository.deleteById(id);
        return "redirect:/admin/viewAdminShowUser";
    }
}
