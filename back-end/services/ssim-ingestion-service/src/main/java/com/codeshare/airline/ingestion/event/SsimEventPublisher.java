package com.codeshare.airline.ingestion.event;

import com.codeshare.airline.ingestion.event.model.ParsedSsimBatchEvent;
import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.parsing.record.SsimRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SsimEventPublisher {

    private final KafkaTemplate<String, ParsedSsimBatchEvent> kafkaTemplate;

    @Value("${kafka.topic.ssim-parsed}")
    private String topic;

    public void publish(SsimRawFile file, List<SsimRecord> records) {

        ParsedSsimBatchEvent event = ParsedSsimBatchEvent.builder()
                .fileId(file.getFileId())
                .sourceType(file.getSourceType())
                .receivedAt(file.getReceivedAt())
                .records(records)
                .build();

        kafkaTemplate.send(topic, file.getFileId(), event);
    }
}
