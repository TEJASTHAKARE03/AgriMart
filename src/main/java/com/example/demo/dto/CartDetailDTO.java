package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDetailDTO {

    private Integer cartId;
    private Integer userId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;

}
