package com.codeshare.airline.schedule.processing.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.CompareRequestedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CompareRequestedEventPublisher {

    private final KafkaTemplate<String, CompareRequestedEvent> kafkaTemplate;
    private final String topic;

    public CompareRequestedEventPublisher(
            KafkaTemplate<String, CompareRequestedEvent> kafkaTemplate,
            @Value("${schedule.workflow.topics.compare-requested}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(CompareRequestedEvent event) {
        if (event == null || event.getImportBatchId() == null) {
            return;
        }
        kafkaTemplate.send(topic, event.getAirlineCode(), event);
    }
}
