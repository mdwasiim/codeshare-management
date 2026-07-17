package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.codeshare.airline.distribution.engine.feign")
public class DistributionEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributionEngineApplication.class, args);
    }
}
