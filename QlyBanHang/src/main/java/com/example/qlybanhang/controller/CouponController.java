package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.Coupon;
import com.example.qlybanhang.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @GetMapping("/admin/searchCoupon")
    public String searchCoupon(Model model) {
        model.addAttribute("listCoupon", couponRepository.findAll());
        return "coupon/view_coupon";
    }

    @GetMapping("/admin/addCoupon")
    public String createNewCoupon() {
        return "coupon/create_coupon";
    }

    @PostMapping("/admin/addCoupon")
    public String createNewCoupon(@ModelAttribute Coupon coupon) {
        couponRepository.save(coupon);
        return "redirect:/admin/viewAdminShowCoupon";
    }

    @GetMapping("/admin/editCoupon")
    public String editCoupon(@RequestParam(name = "idCoupon", required = false) int idCoupon,
                             Model model) {
        Coupon coupon = couponRepository.findById(idCoupon).orElse(null);
        model.addAttribute("editCoupon", coupon);
        return "coupon/edit_coupon";
    }

    @PostMapping("/admin/editCoupon")
    public String editCoupon(@ModelAttribute Coupon coupon){
//                             @RequestParam(name = "date", required = false) String date)
//            throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        coupon.setExpiredDate(sdf.parse(date));
        couponRepository.save(coupon);
        return "redirect:/admin/viewAdminShowCoupon";
    }

    @GetMapping("/admin/deleteCoupon")
    public String deleteCoupon(@RequestParam(name = "id", required = false) int idCoupon) {
        couponRepository.deleteById(idCoupon);
        return "redirect:/admin/viewAdminShowCoupon";
    }

}
