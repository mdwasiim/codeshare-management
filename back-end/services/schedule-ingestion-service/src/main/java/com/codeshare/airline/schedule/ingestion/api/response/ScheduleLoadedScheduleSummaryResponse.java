package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleLoadedScheduleSummaryResponse {

    private ScheduleFileMetaDataDTO file;
    private long messageCount;
    private long flightCount;
}
