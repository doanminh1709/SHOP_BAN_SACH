package com.example.qlybanhang.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "users_id")
    private User users;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;

    @Column(unique = true)
    private String couponCode;

    private double discount=0;

    @OneToMany(mappedBy = "bill")
    private List<BillItems> billItems;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToOne(mappedBy = "bill" , fetch = FetchType.LAZY)
    private DetailCustomer detailCustomer;
}

