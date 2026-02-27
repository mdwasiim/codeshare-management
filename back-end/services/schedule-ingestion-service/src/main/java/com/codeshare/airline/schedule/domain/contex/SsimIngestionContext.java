package com.codeshare.airline.schedule.domain.contex;

import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimParsedFile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SsimIngestionContext extends AbstractIngestionContext<
        SsimInboundFile, SsimParsedFile> {

    /**
     * Matched airline schedule profile
     */
    private ScheduleProfile profile;
}