package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ScheduleMessageIngestionResponse {

    private UUID fileId;
    private UUID loadId;
    private String fileName;
    private String airlineCode;
    private MessageType messageType;
    private ProcessingStatus status;
}
