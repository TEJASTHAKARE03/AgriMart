package com.example.demo.dto;

import lombok.Data;

@Data
public class UserRegistrationRequestDTO {

    private String name;
    private String email;
    private String password;
    private String userType;
    private String phone;
    private String address;

}
