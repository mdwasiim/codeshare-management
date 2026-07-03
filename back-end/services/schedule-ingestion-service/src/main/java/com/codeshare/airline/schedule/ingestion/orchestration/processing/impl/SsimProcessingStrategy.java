package com.codeshare.airline.schedule.ingestion.orchestration.processing.impl;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.schedule.ingestion.persistence.services.ssim.SsimPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimProcessingStrategy implements ProcessingStrategy<SsimIngestionContext> {

    private final SsimPersistenceService ssimPersistenceService;

    @Override
    public MessageType getMessageType() {
        return MessageType.SSIM;
    }

    @Override
    public void process(SsimIngestionContext context) {
        ssimPersistenceService.saveBatch(context.getParsedData(), context.getMetadata());
    }
}
