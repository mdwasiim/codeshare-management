package com.codeshare.airline.inbound.orchestration.processing.impl;

import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.inbound.services.schedule.SchedulePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsmProcessingStrategy implements ProcessingStrategy<AsmIngestionContext> {

    private final SchedulePersistenceService schedulePersistenceService;

    @Override
    public MessageType getMessageType() {
        return MessageType.ASM;
    }

    @Override
    public void process(AsmIngestionContext context) {

        schedulePersistenceService.save(
                context.getParsedData(),
                context.getMetadata()
        );
    }
}