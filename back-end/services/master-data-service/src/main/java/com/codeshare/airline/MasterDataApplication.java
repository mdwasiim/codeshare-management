package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = "com.codeshare.airline.master"
)
@EnableFeignClients(basePackages = "com.codeshare.airline.ingestion.feign")
public class MasterDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterDataApplication.class, args);
    }
}
