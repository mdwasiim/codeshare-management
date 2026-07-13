package com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger;

import com.codeshare.airline.platform.core.enums.common.CabinClass;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationBookingDesignatorDTO {
    private Long id;
    private String bookingDesignator;
    private String bookingName;
    private CabinClass cabinClass;
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
