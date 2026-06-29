package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleLoadedScheduleSummaryResponse {

    private ScheduleFileMetaDataDTO file;
    private long messageCount;
    private long flightCount;
}
