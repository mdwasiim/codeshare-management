package com.codeshare.airline.ingestion.orchestration.processing.impl;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.orchestration.processing.ProcessingStrategy;
import org.springframework.stereotype.Component;

@Component
public class DefaultProcessingStrategy
        implements ProcessingStrategy<AbstractIngestionContext<?, ?>> {

    @Override
    public MessageType getMessageType() {
        return MessageType.SSIM;
    }

    @Override
    public void process(AbstractIngestionContext<?, ?> context) {
        // no-op or generic logic
    }
}