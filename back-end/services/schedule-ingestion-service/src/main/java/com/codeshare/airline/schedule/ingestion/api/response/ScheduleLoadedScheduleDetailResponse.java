package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleLoadedScheduleDetailResponse {

    private ScheduleFileMetaDataDTO file;
    private List<ScheduleMessageDTO> messages;
    private long messageCount;
    private long flightCount;
}
