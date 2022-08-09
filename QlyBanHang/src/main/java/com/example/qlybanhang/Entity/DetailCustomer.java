package com.example.qlybanhang.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import javax.persistence.*;

@Controller
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetailCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String recipientName;

    private String recipientAddress;

    private String phoneNumber;

    private String payments;


    @OneToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;
}
