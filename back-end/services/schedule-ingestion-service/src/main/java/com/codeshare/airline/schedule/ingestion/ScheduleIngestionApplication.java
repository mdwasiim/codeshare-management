package com.codeshare.airline.schedule.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableFeignClients(basePackages = "com.codeshare.airline.ingestion.feign")
@EnableScheduling
public class ScheduleIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleIngestionApplication.class, args);
    }
}
