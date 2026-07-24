package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.aircraft.ConfigurationRevisionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AircraftConfigurationRevisionDTO {
    private Long id;
    private Long aircraftConfigurationId;
    private Integer revisionNumber;
    private String revisionCode;
    private String revisionName;
    private ConfigurationRevisionStatus revisionStatus;
    private String changeReason;
    private LocalDate publishedDate;
    private String publishedBy;
    private Boolean currentRevision;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
