package com.connecto.friendrequest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients; // 👈 CRITICAL FOR INTER-SERVICE COMMUNICATION

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // Enables calling other microservices by name (e.g., 'user-microservice')
public class FriendRequestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FriendRequestApplication.class, args);
    }
}