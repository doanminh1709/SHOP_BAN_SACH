package com.example.qlybanhang.config;

import com.example.qlybanhang.Entity.Bill;
import com.example.qlybanhang.repository.BillRepository;
import com.example.qlybanhang.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JobSchedule {

    private final MailService mailService;
    private final BillRepository billRepository;

    public JobSchedule(MailService mailService, BillRepository billRepository) {
        this.mailService = mailService;
        this.billRepository = billRepository;
    }

    //Send to account admin , Schedule a scan every 5 minutes,
    //see if there are new orders, then default to your email account,
    @Scheduled(fixedRate = 30000)
    public void notificationMailOthers() {
        Date date = new Date();
        long currentMilliseconds = date.getTime();
        for (Bill bill : billRepository.findAll()) {
            long satisfyingTime = bill.getBuyDate().getTime();
            if (satisfyingTime > currentMilliseconds - 30000) {
                Bill detailBill = billRepository.searchBillByDate(new Date(satisfyingTime));
                String fullName = detailBill.getUsers().getName();
                String email = detailBill.getUsers().getEmail();
                String object = "Just placed new order";
                try {
                mailService.checkMailAdmin(fullName , email , object);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
