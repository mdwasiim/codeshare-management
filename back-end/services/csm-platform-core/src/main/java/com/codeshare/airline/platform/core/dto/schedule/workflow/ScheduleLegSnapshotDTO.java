package com.codeshare.airline.platform.core.dto.schedule.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleLegSnapshotDTO {
    private Long legId;
    private Integer legSequenceNumber;
    private String legStatus;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String daysOfOperation;
    private String frequencyRate;
    private String departureStation;
    private String arrivalStation;
    private LocalTime scheduledDepartureTime;
    private LocalTime aircraftDepartureTime;
    private String departureUtcOffset;
    private String departureTerminal;
    private LocalTime aircraftArrivalTime;
    private LocalTime scheduledArrivalTime;
    private String arrivalUtcOffset;
    private String arrivalTerminal;
    private String dateVariation;
    private String aircraftType;
    private String aircraftConfiguration;
    private String serviceType;
    private String bookingDesignator;
    private String bookingModifier;
    private String mealServiceNote;
    private String jointOperationAirlines;
    private String minimumConnectingTimeStatus;
    private String secureFlightIndicator;
    private String aircraftOwner;
    private String cockpitCrewEmployer;
    private String cabinCrewEmployer;
    private String onwardAirlineDesignator;
    private String onwardFlightNumber;
    private String onwardOperationalSuffix;
    private String aircraftRotationLayover;
    private String flightTransitLayover;
    private String operatingAirlineDisclosure;
    private String trafficRestrictionCode;

    @Builder.Default
    private List<ScheduleDataElementSnapshotDTO> legDataElements = new ArrayList<>();

    @Builder.Default
    private List<ScheduleSegmentSnapshotDTO> segments = new ArrayList<>();

    @Builder.Default
    private List<ScheduleCodeshareSnapshotDTO> codeshares = new ArrayList<>();
}
