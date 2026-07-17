package com.codeshare.airline.schedule.live.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleUpdatedEventPublisher {

    private final KafkaTemplate<String, ScheduleUpdatedEvent> kafkaTemplate;
    private final String topic;

    public ScheduleUpdatedEventPublisher(
            KafkaTemplate<String, ScheduleUpdatedEvent> kafkaTemplate,
            @Value("${schedule.workflow.topics.schedule-updated}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(ScheduleUpdatedEvent event) {
        kafkaTemplate.send(topic, event.getAirlineCode(), event);
    }
}
