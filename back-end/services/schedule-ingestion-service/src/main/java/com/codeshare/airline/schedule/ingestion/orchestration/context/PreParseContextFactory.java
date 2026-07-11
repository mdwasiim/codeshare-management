package com.codeshare.airline.schedule.ingestion.orchestration.context;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;

import java.util.List;

public interface PreParseContextFactory<T extends AbstractIngestionContext<?, ?>> {

    MessageType supportedType();

    T build(ScheduleFileMetaDataDTO metadata, List<String> lines);
}
