package com.connecto.apigateway;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Enables Consul registration
public class ApiGatewayMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayMicroserviceApplication.class, args);
    }
}