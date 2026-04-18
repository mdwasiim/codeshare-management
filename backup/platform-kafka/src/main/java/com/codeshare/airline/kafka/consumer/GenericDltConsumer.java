package com.codeshare.airline.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenericDltConsumer {

    @KafkaListener(
            topicPattern = ".*\\.DLT",
            groupId = "kafka-dlt-monitor"
    )
    public void consume(ConsumerRecord<String, Object> record) {

        log.error("""
            🔥 DLT MESSAGE RECEIVED
            Topic      : {}
            Key        : {}
            Payload    : {}
            Headers    : {}
            """,
                record.topic(),
                record.key(),
                record.value(),
                record.headers()
        );
    }
}
