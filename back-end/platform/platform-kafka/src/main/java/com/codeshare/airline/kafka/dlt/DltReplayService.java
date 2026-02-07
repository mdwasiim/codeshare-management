package com.codeshare.airline.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class DltReplayService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Replay message back to its original topic
     */
    public void replayToOriginalTopic(ConsumerRecord<String, Object> record) {

        String originalTopic = getHeader(
                record,
                "kafka_dlt-original-topic"
        );

        if (originalTopic == null) {
            throw new IllegalStateException(
                    "Missing original topic header, cannot replay"
            );
        }

        log.warn("♻️ Replaying DLT message to original topic: {}", originalTopic);

        kafkaTemplate.send(
                originalTopic,
                record.key(),
                record.value()
        );
    }

    /**
     * Replay message to a custom topic
     */
    public void replayToTopic(
            ConsumerRecord<String, Object> record,
            String targetTopic) {

        log.warn("♻️ Replaying DLT message to topic: {}", targetTopic);

        kafkaTemplate.send(
                targetTopic,
                record.key(),
                record.value()
        );
    }

    private String getHeader(
            ConsumerRecord<String, Object> record,
            String headerName) {

        Header header = record.headers().lastHeader(headerName);
        return header == null
                ? null
                : new String(header.value(), StandardCharsets.UTF_8);
    }
}
