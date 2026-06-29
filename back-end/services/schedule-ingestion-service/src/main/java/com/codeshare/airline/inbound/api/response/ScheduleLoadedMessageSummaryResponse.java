package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ScheduleLoadedMessageSummaryResponse {

    private ScheduleFileMetaDataDTO file;
    private UUID messageId;
    private int messageSequenceNumber;
    private ScheduleMessageDTO message;
    private long flightCount;
}
