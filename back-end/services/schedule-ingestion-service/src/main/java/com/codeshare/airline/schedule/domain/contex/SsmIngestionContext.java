package com.codeshare.airline.schedule.domain.contex;

import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFile;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class SsmIngestionContext  extends AbstractIngestionContext<
        ScheduleInboundFile, List<SsmInboundMessage>> {

    /**
     * Matched airline schedule profile
     */
    private ScheduleProfile profile;
}
