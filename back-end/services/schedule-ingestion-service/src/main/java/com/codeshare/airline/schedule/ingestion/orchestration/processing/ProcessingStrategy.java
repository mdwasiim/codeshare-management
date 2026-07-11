package com.codeshare.airline.schedule.ingestion.orchestration.processing;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;

public interface ProcessingStrategy<T extends AbstractIngestionContext<?, ?>> {

    MessageType getMessageType();

    void process(T context);
}