package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
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
