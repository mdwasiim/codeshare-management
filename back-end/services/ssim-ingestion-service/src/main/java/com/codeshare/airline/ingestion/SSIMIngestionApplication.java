package com.codeshare.airline.ssim.com.codeshare.airline.ssim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline")
@EnableFeignClients(basePackages = "com.codeshare.airline.ssim.feign")
public class SSIMIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSIMIngestionApplication.class, args);
    }
}
