package com.codeshare.airline.schedule.message.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DistributionRequestedEventPublisher {

    private final KafkaTemplate<String, DistributionRequestedEvent> kafkaTemplate;
    private final String topic;

    public DistributionRequestedEventPublisher(
            KafkaTemplate<String, DistributionRequestedEvent> kafkaTemplate,
            @Value("${schedule.workflow.topics.distribution-requested}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(DistributionRequestedEvent event) {
        kafkaTemplate.send(topic, event.getAirlineCode(), event);
    }
}
