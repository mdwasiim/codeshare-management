package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline")
@EnableFeignClients(basePackages = "com.codeshare.airline.schedule.processing.feign")
public class ScheduleProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleProcessingServiceApplication.class, args);
    }
}
