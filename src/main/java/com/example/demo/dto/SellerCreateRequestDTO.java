package com.example.demo.dto;

import lombok.Data;

@Data
public class SellerCreateRequestDTO {

    private Integer userId;
    private String shopName;
    private String gstNumber;

}
