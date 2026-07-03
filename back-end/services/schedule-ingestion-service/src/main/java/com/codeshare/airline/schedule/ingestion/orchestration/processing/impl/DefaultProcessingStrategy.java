package com.codeshare.airline.schedule.ingestion.orchestration.processing.impl;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategy;
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