package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimAppendixHCodeshareDTO extends CSMAuditableDTO {

    private SsimR3FlightLegRecordDTO operatingLeg;

    private String marketingCarrier;

    private String marketingFlightNumber;

    private Integer priorityOrder;

    private String disclosureIndicator;

    private String codeshareType;
    private String marketingAirlineDesignator;
    private String operatingAirlineDesignator;
    private String operatingFlightNumber;
}
