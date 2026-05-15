package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.validations.model.ValidationMessage;
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
