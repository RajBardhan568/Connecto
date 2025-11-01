package com.connecto.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String surname;
    
    @Column(unique = true) // US 01 requirement: Email must be unique
    private String emailAddress;
    
    private String passwordHash; // Store hashed password, NOT the raw password
    private String dateOfBirth;
    private String gender;


    // NEW FIELD FOR US 04
    @Column(columnDefinition = "TEXT") 
    private String bio;
}