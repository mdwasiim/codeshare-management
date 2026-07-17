package com.codeshare.airline.schedule.ingestion.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ProcessingRequestedEvent;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProcessingRequestedEventPublisher {

    private final KafkaTemplate<String, ProcessingRequestedEvent> kafkaTemplate;
    private final String topic;

    public ProcessingRequestedEventPublisher(
            KafkaTemplate<String, ProcessingRequestedEvent> kafkaTemplate,
            @Value("${schedule.workflow.topics.processing-requested}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(ScheduleFileMetaDataDTO metadata) {
        if (metadata == null || metadata.getFileId() == null || metadata.getMessageType() == null) {
            return;
        }

        UUID processingRequestId = UUID.randomUUID();
        UUID correlationId = metadata.getLoadId() != null ? metadata.getLoadId() : processingRequestId;
        ProcessingRequestedEvent event = ProcessingRequestedEvent.builder()
                .correlationId(correlationId)
                .causationId(metadata.getLoadId())
                .processingRequestId(processingRequestId)
                .importedScheduleId(metadata.getFileId())
                .importBatchId(metadata.getLoadId())
                .messageType(metadata.getMessageType())
                .airlineCode(metadata.getAirlineCode())
                .partnerCode(metadata.getPartnerCode())
                .sourceName(metadata.getFileName())
                .requestedAt(Instant.now())
                .build();

        kafkaTemplate.send(topic, metadata.getAirlineCode(), event);
    }
}
