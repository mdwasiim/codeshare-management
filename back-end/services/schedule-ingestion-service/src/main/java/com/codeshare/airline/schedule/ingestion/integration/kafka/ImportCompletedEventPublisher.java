package com.codeshare.airline.schedule.ingestion.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ImportCompletedEvent;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ImportCompletedEventPublisher {

    private final KafkaTemplate<String, ImportCompletedEvent> kafkaTemplate;
    private final String topic;

    public ImportCompletedEventPublisher(
            KafkaTemplate<String, ImportCompletedEvent> kafkaTemplate,
            @Value("${schedule.workflow.topics.import-completed}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(ScheduleFileMetaDataDTO metadata) {
        if (metadata == null || metadata.getFileId() == null || metadata.getMessageType() == null) {
            return;
        }

        UUID correlationId = metadata.getLoadId() != null ? metadata.getLoadId() : UUID.randomUUID();
        ImportCompletedEvent event = ImportCompletedEvent.builder()
                .correlationId(correlationId)
                .causationId(metadata.getLoadId())
                .importedScheduleId(metadata.getFileId())
                .importBatchId(metadata.getLoadId())
                .messageType(metadata.getMessageType())
                .airlineCode(metadata.getAirlineCode())
                .partnerCode(metadata.getPartnerCode())
                .sourceName(metadata.getFileName())
                .completedAt(metadata.getProcessedAt() != null ? metadata.getProcessedAt() : Instant.now())
                .build();

        kafkaTemplate.send(topic, metadata.getAirlineCode(), event);
    }
}
