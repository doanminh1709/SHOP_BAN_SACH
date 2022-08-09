package com.example.qlybanhang.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Data
@Entity
public class Member {
    @Id
    private int id;//id của user nó lấy làm khóa chính luôn

    private String memberCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;
}
