package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleLoadedMessageSummaryResponse {

    private ScheduleFileMetaDataDTO file;
    private Long messageId;
    private int messageSequenceNumber;
    private ScheduleMessageDTO message;
    private long flightCount;
}
