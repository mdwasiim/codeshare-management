package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleFileMessageResponse {

    private ScheduleFileMetaDataDTO file;
    private List<ScheduleMessageDTO> messages;
}
