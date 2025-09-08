package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SellerResponseDTO {

    private Integer sellerId;
    private Integer userId;
    private String userName;
    private String shopName;
    private BigDecimal rating;
    private LocalDateTime memberSince;

}
