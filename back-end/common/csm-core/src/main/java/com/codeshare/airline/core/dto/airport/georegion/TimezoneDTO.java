package com.codeshare.airline.core.dto.airport.georegion;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TimezoneDTO {

    private UUID id;
    private String zoneId;
    private String utcOffset;
    private Boolean isDstSupported;
    private UUID dstRuleId;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}