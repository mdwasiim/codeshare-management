package com.codeshare.airline.platform.core.dto.master.georegion;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.schedule.SeasonType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SeasonDTO {

    private UUID id;
    private String seasonCode;
    private String seasonName;
    private SeasonType seasonType;
    private Integer scheduleYear;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
