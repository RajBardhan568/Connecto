package com.connecto.user.dto;

import lombok.Data;

@Data
public class LoginDTO {

    // These fields must match the Angular frontend form
    private String emailOrPhone;
    private String password;
    
}