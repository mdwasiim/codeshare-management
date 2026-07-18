package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline")
@EnableFeignClients(basePackages = "com.codeshare.airline.schedule.compare.feign")
public class ScheduleCompareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleCompareServiceApplication.class, args);
    }
}
