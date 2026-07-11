package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleMessageValidationResponse {

    private MessageType messageType;
    private String fileName;
    private String airlineCode;
    private int blockCount;
    private int parsedBlockCount;
    private boolean valid;
    private List<ValidationMessage> messages;
    private List<ScheduleMessageDTO> parsedMessages;
}
