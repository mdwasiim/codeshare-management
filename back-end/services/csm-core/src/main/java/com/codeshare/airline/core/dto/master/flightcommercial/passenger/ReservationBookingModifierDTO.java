package com.codeshare.airline.core.dto.master.flightcommercial.passenger;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReservationBookingModifierDTO {
    private UUID id;
    private String modifierCode;
    private String modifierName;
    private String category;
    private String iataDefinition;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
