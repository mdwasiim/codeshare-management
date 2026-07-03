package com.codeshare.airline.schedule.ingestion.orchestration.processing.impl;

import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.schedule.ingestion.persistence.services.schedule.SchedulePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsmProcessingStrategy implements ProcessingStrategy<SsmIngestionContext> {

    private final SchedulePersistenceService schedulePersistenceService;

    @Override
    public MessageType getMessageType() {
        return MessageType.SSM;
    }


    @Override
    public void process(SsmIngestionContext context) {

        schedulePersistenceService.save(context.getParsedData(),context.getMetadata());
    }
}