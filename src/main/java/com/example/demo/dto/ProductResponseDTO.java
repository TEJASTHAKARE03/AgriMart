package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {

    private Integer productId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int stock;

    // We can include some seller info, but not the whole entity
    private Integer sellerId;
    private String sellerShopName;

}
