package com.example.qlybanhang.controller;

import com.example.qlybanhang.repository.DetailCustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailCustomerController {

    private final DetailCustomerRepository detailCustomerRepository;

    public DetailCustomerController(DetailCustomerRepository detailCustomerRepository) {
        this.detailCustomerRepository = detailCustomerRepository;
    }

    //find all customer
    @GetMapping("/list_detail_customer")
    public String getListDetail(Model model) {
        model.addAttribute("list_customer", detailCustomerRepository.findAll());
        return "detailCustomer/findAllCustomer";
    }

    //delete detail customer
    @GetMapping("/delete_detail_customer/{id}")
    public String removeDetailCustomer(@PathVariable("id") int id) {
        detailCustomerRepository.findById(id);
        return "redirect:/list_detail_customer";
    }

}
