package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {

    private Integer productId;
    private String productName;
    private BigDecimal price; // Current price of the product
    private int quantity;

}
