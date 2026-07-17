package com.codeshare.airline.distribution.engine.integration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaRetryConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaOperations<Object, Object> kafkaOperations,
            @Value("${app.kafka.retry.interval-ms:1000}") long retryIntervalMs,
            @Value("${app.kafka.retry.max-attempts:3}") long maxAttempts,
            @Value("${app.kafka.retry.dlt-suffix:.DLT}") String dltSuffix
    ) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaOperations,
                (record, ex) -> new TopicPartition(record.topic() + dltSuffix, record.partition())
        );
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(retryIntervalMs, maxAttempts));
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> log.warn(
                "Kafka retry topic={} partition={} offset={} attempt={} error={}",
                record.topic(),
                record.partition(),
                record.offset(),
                deliveryAttempt,
                ex.getMessage()
        ));

        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }
}
