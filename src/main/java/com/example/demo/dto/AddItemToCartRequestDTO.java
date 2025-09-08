package com.example.demo.dto;

import lombok.Data;

@Data
public class AddItemToCartRequestDTO {

    private Integer productId;
    private int quantity;

}
