package com.example.qlybanhang.controller;

import com.example.qlybanhang.Entity.*;
import com.example.qlybanhang.dto.OrderDTO;
import com.example.qlybanhang.dto.OrderItemDTO;
import com.example.qlybanhang.dto.Statistics;
import com.example.qlybanhang.repository.BillItemRepository;
import com.example.qlybanhang.repository.BillRepository;
import com.example.qlybanhang.repository.DetailCustomerRepository;
import com.example.qlybanhang.repository.UserRepository;
import com.example.qlybanhang.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@EnableScheduling
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private DetailCustomerRepository detailCustomerRepository;

    @Autowired
    private MailService mailService;

    @GetMapping("/showBill")
    public String adminManagerBill(Model model) {
        model.addAttribute("list_bill", billRepository.findAll());
        return "Bill/showBill";
    }


    @GetMapping("/admin/createBill")
    public String createBill(Model model) {
        model.addAttribute("listUser", userRepository.findAll());
        return "Bill/createNewBill";
    }

    //admin create new bill
    @PostMapping("/admin/createBill")
    public String createBill(@ModelAttribute Bill bill,
                             @RequestParam(name = "user_id", required = false) String userID) {
        User user = new User();
        user.setId(Integer.parseInt(userID));
        bill.setUsers(user);
        billRepository.save(bill);
        return "redirect:/showBill";
    }

    @GetMapping("/admin/editBill")
    public String editBiLL(@RequestParam(name = "id", required = false) int id, Model model) {
        model.addAttribute("editBill", billRepository.findById(id).orElse(null));
        return "Bill/edit";
    }

    @PostMapping("/admin/editBill")
    public String editBill(@ModelAttribute Bill bill) {
        billRepository.save(bill);
        return "redirect:/admin/viewAdminShowBill";
    }

    @GetMapping("/admin/deleteBill")
    public String deleteBill(@RequestParam(name = "id", required = false) int id) {
        billRepository.deleteById(id);
        return "redirect:/admin/viewAdminShowBill";
    }

    @GetMapping("/pay-to-bill")
    public String addToBill() {
        return "cart/pay";
    }

    @PostMapping("/payToBill")
    public String addToBill(Model model, HttpSession session, @RequestParam(name = "recipientName", required = false)
            String recipientName, @RequestParam(name = "recipientAddress", required = false) String recipientAddress,
                            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                            @RequestParam(name = "payments", required = false) String payments) throws MessagingException {
        Coupon coupon = (Coupon) session.getAttribute("coupon");
        OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
        double total = (double) session.getAttribute("total");
        User user = (User) session.getAttribute("user");
        Bill bill = new Bill();
        if (coupon != null) {
            bill.setCouponCode(coupon.getCouponCode());
            bill.setDiscount(coupon.getDiscountAmount());
        } else {
            bill.setCouponCode(null);
            bill.setDiscount(0);
            model.addAttribute("notCoupon", "Không có mã giảm giá");
        }
// BillItems
        bill.setCoupon(coupon);
        bill.setBuyDate(new Date());
        bill.setUsers(user);
        billRepository.save(bill);
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItemList()) {
            BillItems billItems = new BillItems();
            billItems.setProduct(orderItemDTO.getProduct());
            billItems.setQuality(orderItemDTO.getNumber());
            billItems.setBuyPrice(orderItemDTO.getProduct().getPrice());
            billItems.setBill(bill);
            billItemRepository.save(billItems);
        }
//Detail Customer
        DetailCustomer detailCustomer = new DetailCustomer();
        detailCustomer.setBill(bill);
        detailCustomer.setRecipientName(recipientName);
        detailCustomer.setRecipientAddress(recipientAddress);
        detailCustomer.setPhoneNumber(phoneNumber);
        detailCustomer.setPayments(payments);
        detailCustomerRepository.save(detailCustomer);
        //Send mail in format html to email
        model.addAttribute("infoCustomer", detailCustomer);
        model.addAttribute("order", orderDTO);
        model.addAttribute("total", total);
        model.addAttribute("coupon", coupon);
        mailService.sendMailHtml(bill.getUsers().getEmail(), "Detail order for you", orderDTO,
                total, coupon, detailCustomer);
        return "Bill/success";
    }

    @GetMapping(value = {"/view-to-pay"})
    public String payToCart(HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            request.setAttribute("user", user);
            request.setAttribute("total", session.getAttribute("total"));
            request.setAttribute("order", session.getAttribute("cart"));
            if (session.getAttribute("coupon") != null) {
                Coupon coupon = (Coupon) session.getAttribute("coupon");
                request.setAttribute("coupon", coupon);
            }
            return "Bill/pay";
        } else {
            return "accessAccount/check";
        }
    }

    @GetMapping("/searchBillByMonth")
    public String searchBillByMonth(Model model) {
        List<Statistics> statisticsByMonth = new ArrayList<>();
        List<Object[]> result = billRepository.searchBillByMonth();
        for (Object[] obj : result) {
            Statistics statistics = new Statistics();
            statistics.setThang((Integer) obj[0]);
            statistics.setSl((Long) obj[1]);
            statisticsByMonth.add(statistics);
        }
        model.addAttribute("monthlyStatistics", statisticsByMonth);
        return "Bill/searchBillByMonth";
    }

    @GetMapping("/searchByUserName")
    public String searchByUserName() {
        return "Bill/searchBillByUser";
    }

    @PostMapping("/searchByUserName")
    public String searchByUserName(@RequestParam(name = "name", required = false) String name,
                                   Model model) {
        List<Statistics> statisticsByUser = new ArrayList<>();
        List<Object[]> result = billRepository.searchBillByUser("%" + name + "%");
        for (Object[] objects : result) {
            Statistics statistics = new Statistics();
            statistics.setName((String) objects[0]);
            statistics.setSl((Long) objects[1]);
            statisticsByUser.add(statistics);
        }
        model.addAttribute("searchBillByUser", statisticsByUser);
        return "Bill/resultSearchByUserName";
    }

    @GetMapping("/searchBillByTime")
    public String getBillByTime() {
        return "Bill/searchByTime";
    }

    @PostMapping("/searchBillByTime")
    public String getBillByTime(@RequestParam(name = "from", required = false) String start,
                                @RequestParam(name = "to", required = false) String end,
                                Model model) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Bill getBillByTime = billRepository.searchBillByDateTime(sdf.parse(start), sdf.parse(end));
        model.addAttribute("billByTime", getBillByTime);
        return "Bill/resultSearchByTime";
    }
}
