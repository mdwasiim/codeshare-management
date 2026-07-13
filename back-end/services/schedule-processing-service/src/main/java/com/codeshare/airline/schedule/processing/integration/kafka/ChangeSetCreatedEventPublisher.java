package com.codeshare.airline.schedule.processing.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ChangeSetCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChangeSetCreatedEventPublisher {

    private final KafkaTemplate<String, ChangeSetCreatedEvent> kafkaTemplate;
    private final String topic;

    public ChangeSetCreatedEventPublisher(
            KafkaTemplate<String, ChangeSetCreatedEvent> kafkaTemplate,
            @Value("${schedule.workflow.topics.change-set-created}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(ChangeSetCreatedEvent event) {
        kafkaTemplate.send(topic, event.getAirlineCode(), event);
    }
}
