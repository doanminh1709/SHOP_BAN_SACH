package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.*;
import com.example.qlybanhang.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ViewTemplateController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BillRepository billRepository;
    private final CouponRepository couponRepository;

    public ViewTemplateController(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository, BillRepository billRepository, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.billRepository = billRepository;
        this.couponRepository = couponRepository;
    }

    @GetMapping("/")
    public String clientPage(HttpSession session, Model model) {
        UserDetails userDetails = null;
        Object b = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (b != "anonymousUser") {
            userDetails = (UserDetails) b;
        }
        if (userDetails != null) {
            session.setAttribute("user", userRepository.findUserByUsername(userDetails.getUsername()));
            model.addAttribute("user", session.getAttribute("user"));
        }
        model.addAttribute("listProduct", productRepository.findAll());
        return "content-page-client";
    }

    @GetMapping("/admin")
    public ModelAndView adminPage(HttpSession session, Model model ,
                                  @RequestParam(name = "p", required = false) Optional<Integer> p) {
        ModelAndView modelAndView = new ModelAndView();
        UserDetails userDetails = null;
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Category> showCategory = categoryRepository.findAll(pageable);
        model.addAttribute("showCategory", showCategory);
        //get account user login current
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (a != "anonymousUser") {
            userDetails = (UserDetails) a;
        }
        if (userDetails != null) {
            session.setAttribute("admin", userRepository.findUserByUsername(userDetails.getUsername()));
            model.addAttribute("admin", session.getAttribute("admin"));
        }
        modelAndView.setViewName("content-page-admin");//resource/template/content-page-admin
        return modelAndView;
    }


    @GetMapping("/admin/viewAdminShowCategory")
    public String adminShowCategory(Model model, @RequestParam(name = "p", required = false) Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Category> showCategory = categoryRepository.findAll(pageable);
        model.addAttribute("showCategory", showCategory);
        return "content-page-admin-category";
    }

    //    show bill
    @GetMapping("/admin/viewAdminShowBill")
    public String adminShowBill(Model model, @RequestParam(name = "p", required = false) Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Bill> showBill = billRepository.findAll(pageable);
        model.addAttribute("showBill", showBill);
        return "content-page-admin_bill";
    }

    //show user
    @GetMapping("/admin/viewAdminShowUser")
    public String adminShowUser(Model model, @RequestParam(name = "p", required = false) Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<User> showListUser = userRepository.findAll(pageable);
        model.addAttribute("showListUser", showListUser);
        return "content-page-admin_user";
    }

    //  Show coupon
    @GetMapping("/admin/viewAdminShowCoupon")
    public String adminShowCoupon(Model model, @RequestParam(name = "p", required = false) Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Coupon> showListCoupon = couponRepository.findAll(pageable);
        model.addAttribute("showListCoupon", showListCoupon);
        return "content-page-admin_coupon";
    }

    //Show product
    @GetMapping("/admin/viewAdminShowProduct")
    public String adminShowProduct(Model model, @RequestParam(name = "p", required = false) Optional<Integer> p) {
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Product> showListProduct = productRepository.findAll(pageable);
        model.addAttribute("showListProduct", showListProduct);
        model.addAttribute("listCategory" , categoryRepository.findAll());
        return "content-page-admin-product";
    }

    //change language
    @GetMapping("/change-lang")
    public void changeLange(HttpServletResponse response,
                            @RequestHeader("Referer") String referer) throws IOException {
        System.out.print(referer);
        response.sendRedirect(referer);
    }
}
