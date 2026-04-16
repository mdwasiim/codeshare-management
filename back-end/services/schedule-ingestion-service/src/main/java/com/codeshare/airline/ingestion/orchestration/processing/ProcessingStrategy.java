package com.codeshare.airline.ingestion.orchestration.processing;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.MessageType;

public interface ProcessingStrategy<T extends AbstractIngestionContext<?, ?>> {

    MessageType getMessageType();

    void process(T context);
}