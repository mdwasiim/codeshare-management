package com.codeshare.airline.inbound.orchestration.processing.impl;

import com.codeshare.airline.inbound.domain.context.SsmIngestionContext;
import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.inbound.services.schedule.SchedulePersistenceService;
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