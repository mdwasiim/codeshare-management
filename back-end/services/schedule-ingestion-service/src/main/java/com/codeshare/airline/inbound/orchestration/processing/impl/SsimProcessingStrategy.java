package com.codeshare.airline.inbound.orchestration.processing.impl;

import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.inbound.services.ssim.SsimPersistenceService;
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

        // 🔥 batch processing
        ssimPersistenceService.saveBatch(
                context.getParsedData(),
                context.getMetadata()
        );
    }
}