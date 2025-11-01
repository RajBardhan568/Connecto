package com.connecto.user.dto;

import lombok.Data;

@Data // Lombok annotation for getters, setters, toString, etc.
public class UserRegistrationDTO {
    // Matches fields in Angular's RegistrationDTO
    private String firstName;
    private String surname;
    private String emailAddress;
    private String password;
    private String confirmPassword; // Not saved, but required for validation
    private String dateOfBirth; // Stored as String/Date object
    private String gender;
}