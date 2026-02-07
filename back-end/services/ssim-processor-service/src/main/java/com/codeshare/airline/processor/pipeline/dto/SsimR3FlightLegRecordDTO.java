package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimR3FlightLegRecordDTO extends CSMAuditableDTO {

    private Long id;
    private Long datasetId;

    private String recordType;                 // '3'
    private String airlineDesignator;
    private String flightNumber;
    private String operationalSuffix;

    private String originAirport;
    private String destinationAirport;

    private String scheduledDepartureTime;
    private String scheduledArrivalTime;
    private String overnightIndicator;

    private String periodStartDate;
    private String periodEndDate;
    private String daysOfOperation;

    private String aircraftType;
    private String aircraftConfiguration;

    private String serviceType;
    private String trafficRestrictionCode;

    private String remarks;

    private List<SsimR3SegmentDataRecordDTO> segmentData;
}
