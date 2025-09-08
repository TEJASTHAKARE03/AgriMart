package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateRequestDTO {

    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int stock;

    // The ID of the seller creating the product is required.
    private Integer sellerId;

}
