package com.connecto.posts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients; // Enables Feign

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // Enables calling User and Friend Request microservices
public class PostsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostsApplication.class, args);
    }
}