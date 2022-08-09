package com.example.qlybanhang.service;

import com.example.qlybanhang.Entity.Coupon;
import com.example.qlybanhang.Entity.DetailCustomer;
import com.example.qlybanhang.config.MyConstants;
import com.example.qlybanhang.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    @Autowired
    private SpringTemplateEngine templateEngine;//send file hmtl

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendMail(String to, String subject, String body) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        try {
            helper.setTo(to);
            helper.setText(body);
            helper.setSubject(subject);
            helper.setFrom(MyConstants.MY_EMAIL);
            javaMailSender.send(message);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }

    @Async
    public void sendMailHtml(String to, String subject, OrderDTO orderDTO, double total, Coupon coupon,
                             DetailCustomer detailCustomer) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            //load template email with content
            Context context = new Context();
            context.setVariable("infoCustomer", detailCustomer);
            context.setVariable("order", orderDTO);
            context.setVariable("total", total);
            context.setVariable("coupon", coupon);
            String html = templateEngine.process("layoutDetailBill.html", context);
            //send mail
            helper.setTo(to);
            helper.setText(html, true);
            helper.setSubject(subject);
            helper.setFrom(MyConstants.MY_EMAIL);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void checkMailAdmin(String fullName, String mail, String object) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String mailContent = "<p><b>Name : </b>" + fullName + "</p>";
        mailContent += "<p><b>Email : </b>" + mail + "</p>";
        mailContent += "<p><b>Content : </b>" + object + "</p>";

        helper.setFrom(MyConstants.MY_EMAIL, "Admin");
        helper.setTo(MyConstants.MY_EMAIL);
        helper.setText(mailContent);

        javaMailSender.send(message);
    }

}