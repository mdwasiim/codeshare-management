package com.codeshare.airline.schedule.ingestion.orchestration.parsers;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;

import java.util.List;

public interface MessageParser<T extends AbstractIngestionContext<?, ?>> {

    MessageType supportedType();

    T parse(List<String> lines, ScheduleFileMetaDataDTO metadata);
}