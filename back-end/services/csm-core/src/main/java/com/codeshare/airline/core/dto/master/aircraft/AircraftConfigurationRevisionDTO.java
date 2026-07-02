package com.codeshare.airline.core.dto.master.aircraft;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.master.aircraft.ConfigurationRevisionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AircraftConfigurationRevisionDTO {
    private UUID id;
    private UUID aircraftConfigurationId;
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
