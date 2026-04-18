package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.codeshare.airline.inbound")
//@EnableFeignClients(basePackages = "com.codeshare.airline.ingestion.feign")
@EnableScheduling
public class ScheduleIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleIngestionApplication.class, args);
    }
}
