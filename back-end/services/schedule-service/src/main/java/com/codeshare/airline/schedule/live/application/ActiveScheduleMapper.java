package com.codeshare.airline.schedule.live.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ActiveScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.schedule.live.domain.entity.LiveCodeshareDesignatorEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightLegEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveLegDataElementEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentDeiEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActiveScheduleMapper {

    public ActiveScheduleDTO toActiveSchedule(String airlineCode, List<LiveFlightEntity> flights) {
        List<ScheduleFlightSnapshotDTO> mappedFlights = flights == null
                ? List.of()
                : flights.stream().map(this::toFlightSnapshot).toList();

        return ActiveScheduleDTO.builder()
                .airlineCode(airlineCode)
                .asOf(Instant.now())
                .flights(new ArrayList<>(mappedFlights))
                .build();
    }

    public ScheduleFlightSnapshotDTO toFlightSnapshot(LiveFlightEntity flight) {
        return ScheduleFlightSnapshotDTO.builder()
                .flightId(flight.getId())
                .airlineCode(flight.getAirlineCode())
                .flightNumber(flight.getFlightNumber())
                .operationalSuffix(flight.getOperationalSuffix())
                .itineraryVariationId(flight.getItineraryVariationId())
                .flightStatus(flight.getFlightStatus() == null ? null : flight.getFlightStatus().name())
                .legs(flight.getLegs() == null
                        ? new ArrayList<>()
                        : flight.getLegs().stream().map(this::toLegSnapshot).toList())
                .build();
    }

    public ScheduleLegSnapshotDTO toLegSnapshot(LiveFlightLegEntity leg) {
        ScheduleLegSnapshotDTO snapshot = ScheduleLegSnapshotDTO.builder()
                .legId(leg.getId())
                .legSequenceNumber(leg.getLegSequenceNumber())
                .legStatus(leg.getLegStatus() == null ? null : leg.getLegStatus().name())
                .periodStart(leg.getPeriodStart())
                .periodEnd(leg.getPeriodEnd())
                .daysOfOperation(leg.getDaysOfOperation())
                .frequencyRate(leg.getFrequencyRate())
                .departureStation(leg.getDepartureStation())
                .arrivalStation(leg.getArrivalStation())
                .scheduledDepartureTime(leg.getScheduledDepartureTime())
                .aircraftDepartureTime(leg.getAircraftDepartureTime())
                .departureUtcOffset(leg.getDepartureUtcOffset())
                .departureTerminal(leg.getDepartureTerminal())
                .aircraftArrivalTime(leg.getAircraftArrivalTime())
                .scheduledArrivalTime(leg.getScheduledArrivalTime())
                .arrivalUtcOffset(leg.getArrivalUtcOffset())
                .arrivalTerminal(leg.getArrivalTerminal())
                .dateVariation(leg.getDateVariation())
                .aircraftType(leg.getAircraftType())
                .aircraftConfiguration(leg.getAircraftConfiguration())
                .serviceType(leg.getServiceType())
                .bookingDesignator(leg.getBookingDesignator())
                .bookingModifier(leg.getBookingModifier())
                .mealServiceNote(leg.getMealServiceNote())
                .jointOperationAirlines(leg.getJointOperationAirlines())
                .minimumConnectingTimeStatus(leg.getMinimumConnectingTimeStatus())
                .secureFlightIndicator(leg.getSecureFlightIndicator())
                .aircraftOwner(leg.getAircraftOwner())
                .cockpitCrewEmployer(leg.getCockpitCrewEmployer())
                .cabinCrewEmployer(leg.getCabinCrewEmployer())
                .onwardAirlineDesignator(leg.getOnwardAirlineDesignator())
                .onwardFlightNumber(leg.getOnwardFlightNumber())
                .onwardOperationalSuffix(leg.getOnwardOperationalSuffix())
                .aircraftRotationLayover(leg.getAircraftRotationLayover())
                .flightTransitLayover(leg.getFlightTransitLayover())
                .operatingAirlineDisclosure(leg.getOperatingAirlineDisclosure())
                .trafficRestrictionCode(leg.getTrafficRestrictionCode())
                .legDataElements(leg.getLegDataElements() == null
                        ? new ArrayList<>()
                        : leg.getLegDataElements().stream().map(this::toLegDataElementSnapshot).toList())
                .segments(leg.getSegments() == null
                        ? new ArrayList<>()
                        : leg.getSegments().stream().map(this::toSegmentSnapshot).toList())
                .codeshares(leg.getCodeshareDesignators() == null
                        ? new ArrayList<>()
                        : leg.getCodeshareDesignators().stream().map(this::toCodeshareSnapshot).toList())
                .build();
        return snapshot;
    }

    private ScheduleSegmentSnapshotDTO toSegmentSnapshot(LiveSegmentEntity segment) {
        return ScheduleSegmentSnapshotDTO.builder()
                .segmentId(segment.getId())
                .boardPoint(segment.getBoardPoint())
                .offPoint(segment.getOffPoint())
                .dataElements(segment.getDeis() == null
                        ? new ArrayList<>()
                        : segment.getDeis().stream().map(this::toDataElementSnapshot).toList())
                .build();
    }

    private ScheduleDataElementSnapshotDTO toLegDataElementSnapshot(LiveLegDataElementEntity dei) {
        return ScheduleDataElementSnapshotDTO.builder()
                .dataElementId(dei.getId())
                .code(dei.getDataElementIdentifier())
                .value(dei.getDeiData())
                .scope("LEG")
                .sequenceOrder(dei.getSequenceOrder())
                .build();
    }

    private ScheduleDataElementSnapshotDTO toDataElementSnapshot(LiveSegmentDeiEntity dei) {
        return ScheduleDataElementSnapshotDTO.builder()
                .dataElementId(dei.getId())
                .code(dei.getDataElementIdentifier())
                .value(dei.getDeiData())
                .scope("SEGMENT")
                .sequenceOrder(dei.getSequenceOrder())
                .build();
    }

    private ScheduleCodeshareSnapshotDTO toCodeshareSnapshot(LiveCodeshareDesignatorEntity codeshare) {
        return ScheduleCodeshareSnapshotDTO.builder()
                .codeshareId(codeshare.getId())
                .sequenceOrder(codeshare.getSequenceOrder())
                .marketingAirlineCode(codeshare.getMarketingAirlineCode())
                .marketingFlightNumber(codeshare.getMarketingFlightNumber())
                .marketingOperationalSuffix(codeshare.getMarketingOperationalSuffix())
                .boardPoint(codeshare.getBoardPoint())
                .offPoint(codeshare.getOffPoint())
                .marketingBookingDesignator(codeshare.getMarketingBookingDesignator())
                .sourceDeiCode(codeshare.getSourceDeiCode())
                .codeshare(codeshare.isCodeshare())
                .build();
    }
}

