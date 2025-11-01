package com.connecto.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder // Lombok builder pattern
public class LoginResponseDTO {
    private String token;
    private String firstName;
    private Long userId;
}