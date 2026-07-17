package com.codeshare.airline.schedule.message.feign;

import feign.Request;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class MasterDataScheduleCodeListClientConfig {

    @Bean
    public Request.Options masterDataScheduleCodeListOptions(
            @Value("${app.master-data-client.connect-timeout-ms:1000}") int connectTimeoutMs,
            @Value("${app.master-data-client.read-timeout-ms:3000}") int readTimeoutMs
    ) {
        return new Request.Options(
                connectTimeoutMs,
                TimeUnit.MILLISECONDS,
                readTimeoutMs,
                TimeUnit.MILLISECONDS,
                true
        );
    }

    @Bean
    public Retryer masterDataScheduleCodeListRetryer(
            @Value("${app.master-data-client.retry.period-ms:200}") long periodMs,
            @Value("${app.master-data-client.retry.max-period-ms:1000}") long maxPeriodMs,
            @Value("${app.master-data-client.retry.max-attempts:2}") int maxAttempts
    ) {
        return new Retryer.Default(periodMs, maxPeriodMs, maxAttempts);
    }
}
