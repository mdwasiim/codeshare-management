package com.codeshare.airline.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

    public void send(String topic, String key, Object payload) {
        kafkaTemplate.send(topic, key, payload);
    }

    public void send(String topic, String key, Object payload, Map<String, String> headers) {

        ProducerRecord<String, Object> record =
                new ProducerRecord<>(topic, key, payload);

        headers.forEach((k, v) ->
                record.headers().add(k, v.getBytes(StandardCharsets.UTF_8))
        );

        kafkaTemplate.send(record);
    }

}
