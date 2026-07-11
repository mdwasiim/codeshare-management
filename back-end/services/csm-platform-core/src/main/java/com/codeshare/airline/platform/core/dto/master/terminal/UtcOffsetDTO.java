package com.codeshare.airline.platform.core.dto.master.terminal;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UtcOffsetDTO {

    private UUID id;
    private String offsetCode;
    private String offsetValue;
    private Integer offsetMinutes;
    private String description;
    private UUID timezoneId;
    private String timezoneIdentifier;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
