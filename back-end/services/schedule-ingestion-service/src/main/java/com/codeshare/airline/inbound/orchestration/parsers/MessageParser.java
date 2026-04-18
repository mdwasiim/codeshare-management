package com.codeshare.airline.inbound.orchestration.parsers;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;

import java.util.List;

public interface MessageParser<T extends AbstractIngestionContext<?, ?>> {

    MessageType supportedType();

    T parse(List<String> lines, ScheduleFileMetaDataDTO metadata);
}