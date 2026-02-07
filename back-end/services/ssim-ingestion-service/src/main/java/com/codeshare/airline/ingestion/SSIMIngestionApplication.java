package com.codeshare.airline.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline")
//@EnableFeignClients(basePackages = "com.codeshare.airline.ingestion.feign")
@EnableScheduling
public class SSIMIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSIMIngestionApplication.class, args);
    }
}
