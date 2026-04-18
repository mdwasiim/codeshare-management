package com.codeshare.airline.kafka.config;

import com.codeshare.airline.kafka.exception.NonRetryableKafkaException;
import com.codeshare.airline.kafka.exception.RetryableKafkaException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class RetryTopicConfig {

    @Bean
    public RetryTopicConfiguration retryTopicConfiguration(
            KafkaTemplate<String, Object> kafkaTemplate) {

        return RetryTopicConfigurationBuilder
                .newInstance()

                // 🔁 Retry strategy
                .exponentialBackoff(
                        5_000L,   // initial delay
                        2.0,      // multiplier
                        120_000L  // max delay
                )

                .maxAttempts(4)

                // ☠️ Direct-to-DLT exceptions (ONE BY ONE)
                .notRetryOn(NonRetryableKafkaException.class)
                .notRetryOn(IllegalArgumentException.class)

                // 🔁 Retryable exceptions
                .retryOn(RetryableKafkaException.class)
                .retryOn(RuntimeException.class)

                .dltSuffix(".DLT")

                .create(kafkaTemplate);
    }
}
