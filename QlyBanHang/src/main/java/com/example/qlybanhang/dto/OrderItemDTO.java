package com.example.qlybanhang.dto;

import com.example.qlybanhang.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private int number;
    private Product product;

}
