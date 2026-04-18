package com.codeshare.airline.inbound.orchestration.processing;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.enums.MessageType;

public interface ProcessingStrategy<T extends AbstractIngestionContext<?, ?>> {

    MessageType getMessageType();

    void process(T context);
}