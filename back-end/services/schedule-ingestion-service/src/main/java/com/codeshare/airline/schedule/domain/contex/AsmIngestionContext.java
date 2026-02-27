package com.codeshare.airline.schedule.domain.contex;

import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFile;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class AsmIngestionContext
        extends AbstractIngestionContext<
                ScheduleInboundFile,
                List<AsmInboundMessage>> {

    /**
     * Matched profile for airline (business rules)
     */
    private ScheduleProfile profile;
}