package com.codeshare.airline.inbound.orchestration.processing.impl;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.processing.ProcessingStrategy;
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