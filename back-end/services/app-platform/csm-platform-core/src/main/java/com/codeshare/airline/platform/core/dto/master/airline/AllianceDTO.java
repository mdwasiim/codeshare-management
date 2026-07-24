package com.codeshare.airline.platform.core.dto.master.airline;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AllianceDTO {
    private Long id;
    private String allianceCode;
    private String allianceName;
    private String iataCode;
    private String website;
    private Long headquartersCityId;
    private LocalDate foundedDate;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
