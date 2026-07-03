package com.codeshare.airline.schedule.ingestion.domain.context;

import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleProfile;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class SsimIngestionContext extends AbstractIngestionContext<SsimMetaDataDTO, SSIMMessageDTO> {

    private final ScheduleProfile profile;

    private final List<String> messageLines;

}
