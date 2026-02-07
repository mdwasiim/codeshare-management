package com.codeshare.airline.processor.config;

import com.codeshare.airline.processor.exception.NonRetryableProcessingException;
import com.codeshare.airline.processor.exception.RetryableProcessingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class RetryTopicConfig {

    @Bean
    public RetryTopicConfiguration ssimRetryTopics(
            KafkaTemplate<String, Object> kafkaTemplate) {

        return RetryTopicConfigurationBuilder
                .newInstance()
                .maxAttempts(4)
                .exponentialBackoff(1_000, 2.0, 30_000)
                .retryOn(RetryableProcessingException.class)
                .notRetryOn(NonRetryableProcessingException.class)
                .dltSuffix(".DLT")
                .create(kafkaTemplate);
    }
}
