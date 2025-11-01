package com.connecto.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ============================================================
 * User Microservice - Main Application Class
 * ------------------------------------------------------------
 * Responsibilities:
 *  - Bootstraps the Spring Boot application
 *  - Registers with Consul Service Discovery
 *  - Provides core beans like PasswordEncoder & ModelMapper
 * ============================================================
 */
@SpringBootApplication
@EnableDiscoveryClient  // Enables registration with Consul
public class UserApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        LOGGER.info("✅ User Microservice started successfully and registered with Consul!");
    }

    /**
     * BCryptPasswordEncoder Bean
     * ------------------------------------------------------------
     * Used for secure password hashing and validation.
     * This ensures all passwords are stored safely.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ModelMapper Bean
     * ------------------------------------------------------------
     * Simplifies mapping between DTOs and Entities.
     * Prevents manual property copying.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
