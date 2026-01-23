package com.codeshare.airline.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline")
@EnableFeignClients(basePackages = "com.codeshare.airline.tenant.feign")
public class TenantManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantManagementApplication.class, args);
    }
}
