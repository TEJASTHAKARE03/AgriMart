package com.example.demo.dto;

import com.example.demo.model.enums.UserType;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private UserType userType;

}
