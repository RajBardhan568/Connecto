package com.connecto.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    // We typically only allow updating specific fields, not the email/ID.
    private String firstName;
    private String surname;
    private String dateOfBirth;
    private String gender;
    private String bio;
}