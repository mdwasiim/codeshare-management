package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {"com.codeshare.airline.tenant","com.codeshare.airline.web","com.codeshare.airline.security"}
)
@EnableFeignClients(basePackages = {"com.codeshare.airline.tenant", "com.codeshare.airline.security.feign"})
public class TenantManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantManagementApplication.class, args);
    }
}
