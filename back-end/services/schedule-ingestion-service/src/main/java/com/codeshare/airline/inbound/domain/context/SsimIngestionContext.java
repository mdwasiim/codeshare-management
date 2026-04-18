package com.codeshare.airline.inbound.domain.context;

import com.codeshare.airline.inbound.domain.enums.ScheduleProfile;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class SsimIngestionContext extends AbstractIngestionContext<SsimMetaDataDTO, SSIMMessageDTO> {

    private final ScheduleProfile profile;

    private final List<String> messageLines;

}
