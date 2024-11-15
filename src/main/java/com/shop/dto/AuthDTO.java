package com.shop.dto;

import lombok.Data;

@Data
public class AuthDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
} 