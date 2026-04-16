package com.codeshare.airline.ingestion.orchestration.parsers;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;

import java.util.List;

public interface MessageParser<T extends AbstractIngestionContext<?, ?>> {

    MessageType supportedType();

    T parse(List<String> lines, ScheduleFileMetaDataDTO metadata);
}