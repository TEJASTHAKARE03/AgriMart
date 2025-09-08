package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {

    private Integer productId;
    private String productName;
    private int quantity;
    private BigDecimal price; // Price per item at the time of order

}
