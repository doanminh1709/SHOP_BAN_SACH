package com.example.qlybanhang.controller;

import com.example.qlybanhang.repository.BillItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BillItemController {

    @Autowired
    private BillItemRepository billItemRepository;

//show detail
    @GetMapping("/admin/detail_bill_items/{id}")
    public String detailBillItem(@PathVariable("id") int id ,
                               Model model){
        model.addAttribute("detail_bill_items" ,billItemRepository.findById(id).orElse(null));
        return "billItem/detail_bill_item";
    }
//show all
    @GetMapping("/admin/list_bill_items")
    public String listBillItems(Model model){
        model.addAttribute("listBillItem" , billItemRepository.findAll());
        return "billItem/list_bill_item";
    }
//delete detail
    @GetMapping("/admin/remove_bill_item/{id}")
    public String removeBillItemById(@PathVariable("id") int id){
        billItemRepository.deleteById(id);
        return "redirect:/list_bill_items";
    }

}
