package com.codeshare.airline.schedule.ingestion.domain.context;

import com.codeshare.airline.schedule.ingestion.domain.enums.AsmMessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleProfile;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class AsmIngestionContext extends AbstractIngestionContext<ScheduleFileMetaDataDTO, ScheduleMessageDTO> {

    private final ScheduleProfile profile;

    private final AsmMessageType subMessageType;

    private final List<String> messageLines;
}