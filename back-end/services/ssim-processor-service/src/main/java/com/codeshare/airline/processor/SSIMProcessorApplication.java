package com.codeshare.airline.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline")
@EnableFeignClients(basePackages = "com.codeshare.airline.ingestion.feign")
public class SSIMProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSIMProcessorApplication.class, args);
    }
}
