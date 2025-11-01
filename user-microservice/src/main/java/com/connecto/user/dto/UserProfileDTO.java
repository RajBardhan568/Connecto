package com.connecto.user.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String dateOfBirth;
    private String gender;
    private String bio; // New field for US 04
    // private String profilePictureUrl; // Placeholder for image storage later
}