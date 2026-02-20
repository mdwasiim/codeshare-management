package com.codeshare.airline.ssim.inbound.parsing.processor.dto;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class SsimInboundFlightLegDTO {

    UUID id;
    UUID fileId;

    // 1–14 HEADER
    String recordType;
    String operationalSuffix;
    String airlineCode;
    String flightNumber;
    String itineraryVariationIdentifier;
    String legSequenceNumber;
    String serviceType;

    // 15–35 PERIOD
    String operatingPeriodStartRaw;
    String operatingPeriodEndRaw;
    String operatingDays;

    // 36 FREQUENCY
    String frequencyRate;

    // 37–54 DEPARTURE
    String departureStation;
    String passengerStd;
    String aircraftStd;
    String departureUtcVariation;
    String departureTerminal;

    // 55–72 ARRIVAL
    String arrivalStation;
    String aircraftSta;
    String passengerSta;
    String arrivalUtcVariation;
    String arrivalTerminal;

    // 73–110 AIRCRAFT / BOOKING
    String aircraftType;
    String passengerReservationBookingDesignator;
    String passengerReservationBookingModifier;
    String mealServiceNote;

    // 111–146 JOINT / ONWARD
    String jointOperationAirlineDesignators;
    String minimumConnectingTimeStatus;
    String secureFlightIndicator;
    String spare123To127;
    String itineraryVariationOverflow;
    String aircraftOwner;
    String cockpitCrewEmployer;
    String cabinCrewEmployer;
    String onwardAirlineDesignator;
    String onwardFlightNumber;
    String aircraftRotationLayover;
    String onwardOperationalSuffix;

    // 147–172 DISCLOSURE
    String spare147;
    String flightTransitLayover;
    String operatingAirlineDisclosure;
    String trafficRestrictionCode;
    String trafficRestrictionOverflow;
    String spare162To172;

    // 173–194 CONFIG
    String aircraftConfigurationVersion;
    String dateVariation;

    // 195–200
    String recordSerialNumber;

    String rawRecord;
    Instant parsedTimestamp;

    SsimInboundFileDTO inboundFile;

    List<SsimInboundSegmentDeiDTO> segmentDeis = new ArrayList<>();


    public static SsimInboundFlightLegDTO toDto(SsimInboundFlightLeg entity) {

        if (entity == null) return null;

        return SsimInboundFlightLegDTO.builder()
                .id(entity.getId())

                .fileId(entity.getInboundFile() != null ? entity.getInboundFile().getId() : null)

                .recordType(entity.getRecordType())
                .operationalSuffix(entity.getOperationalSuffix())
                .airlineCode(entity.getAirlineCode())
                .flightNumber(entity.getFlightNumber())
                .itineraryVariationIdentifier(entity.getItineraryVariationIdentifier())
                .legSequenceNumber(entity.getLegSequenceNumber())
                .serviceType(entity.getServiceType())

                .operatingPeriodStartRaw(entity.getOperatingPeriodStartRaw())
                .operatingPeriodEndRaw(entity.getOperatingPeriodEndRaw())
                .operatingDays(entity.getOperatingDays())
                .frequencyRate(entity.getFrequencyRate())

                .departureStation(entity.getDepartureStation())
                .passengerStd(entity.getPassengerStd())
                .aircraftStd(entity.getAircraftStd())
                .departureUtcVariation(entity.getDepartureUtcVariation())
                .departureTerminal(entity.getDepartureTerminal())

                .arrivalStation(entity.getArrivalStation())
                .aircraftSta(entity.getAircraftSta())
                .passengerSta(entity.getPassengerSta())
                .arrivalUtcVariation(entity.getArrivalUtcVariation())
                .arrivalTerminal(entity.getArrivalTerminal())

                .aircraftType(entity.getAircraftType())
                .passengerReservationBookingDesignator(entity.getPassengerReservationBookingDesignator())
                .passengerReservationBookingModifier(entity.getPassengerReservationBookingModifier())
                .mealServiceNote(entity.getMealServiceNote())

                .jointOperationAirlineDesignators(entity.getJointOperationAirlineDesignators())
                .minimumConnectingTimeStatus(entity.getMinimumConnectingTimeStatus())
                .secureFlightIndicator(entity.getSecureFlightIndicator())
                .spare123To127(entity.getSpare123To127())
                .itineraryVariationOverflow(entity.getItineraryVariationOverflow())
                .aircraftOwner(entity.getAircraftOwner())
                .cockpitCrewEmployer(entity.getCockpitCrewEmployer())
                .cabinCrewEmployer(entity.getCabinCrewEmployer())
                .onwardAirlineDesignator(entity.getOnwardAirlineDesignator())
                .onwardFlightNumber(entity.getOnwardFlightNumber())
                .aircraftRotationLayover(entity.getAircraftRotationLayover())
                .onwardOperationalSuffix(entity.getOnwardOperationalSuffix())

                .spare147(entity.getSpare147())
                .flightTransitLayover(entity.getFlightTransitLayover())
                .operatingAirlineDisclosure(entity.getOperatingAirlineDisclosure())
                .trafficRestrictionCode(entity.getTrafficRestrictionCode())
                .trafficRestrictionOverflow(entity.getTrafficRestrictionOverflow())
                .spare162To172(entity.getSpare162To172())

                .aircraftConfigurationVersion(entity.getAircraftConfigurationVersion())
                .dateVariation(entity.getDateVariation())

                .recordSerialNumber(entity.getRecordSerialNumber())

                .rawRecord(entity.getRawRecord())
                .parsedTimestamp(entity.getParsedTimestamp())

                .build();
    }

    public static SsimInboundFlightLeg toEntity(SsimInboundFlightLegDTO entity) {

        if (entity == null) return null;

        return SsimInboundFlightLeg.builder()
                .id(entity.getId())
                .inboundFile(
                        entity.getInboundFile() != null
                                ? SsimInboundFileDTO.toEntity(entity.getInboundFile())
                                : null
                )
                .recordType(entity.getRecordType())
                .operationalSuffix(entity.getOperationalSuffix())
                .airlineCode(entity.getAirlineCode())
                .flightNumber(entity.getFlightNumber())
                .itineraryVariationIdentifier(entity.getItineraryVariationIdentifier())
                .legSequenceNumber(entity.getLegSequenceNumber())
                .serviceType(entity.getServiceType())

                .operatingPeriodStartRaw(entity.getOperatingPeriodStartRaw())
                .operatingPeriodEndRaw(entity.getOperatingPeriodEndRaw())
                .operatingDays(entity.getOperatingDays())
                .frequencyRate(entity.getFrequencyRate())

                .departureStation(entity.getDepartureStation())
                .passengerStd(entity.getPassengerStd())
                .aircraftStd(entity.getAircraftStd())
                .departureUtcVariation(entity.getDepartureUtcVariation())
                .departureTerminal(entity.getDepartureTerminal())

                .arrivalStation(entity.getArrivalStation())
                .aircraftSta(entity.getAircraftSta())
                .passengerSta(entity.getPassengerSta())
                .arrivalUtcVariation(entity.getArrivalUtcVariation())
                .arrivalTerminal(entity.getArrivalTerminal())

                .aircraftType(entity.getAircraftType())
                .passengerReservationBookingDesignator(entity.getPassengerReservationBookingDesignator())
                .passengerReservationBookingModifier(entity.getPassengerReservationBookingModifier())
                .mealServiceNote(entity.getMealServiceNote())

                .jointOperationAirlineDesignators(entity.getJointOperationAirlineDesignators())
                .minimumConnectingTimeStatus(entity.getMinimumConnectingTimeStatus())
                .secureFlightIndicator(entity.getSecureFlightIndicator())
                .spare123To127(entity.getSpare123To127())
                .itineraryVariationOverflow(entity.getItineraryVariationOverflow())
                .aircraftOwner(entity.getAircraftOwner())
                .cockpitCrewEmployer(entity.getCockpitCrewEmployer())
                .cabinCrewEmployer(entity.getCabinCrewEmployer())
                .onwardAirlineDesignator(entity.getOnwardAirlineDesignator())
                .onwardFlightNumber(entity.getOnwardFlightNumber())
                .aircraftRotationLayover(entity.getAircraftRotationLayover())
                .onwardOperationalSuffix(entity.getOnwardOperationalSuffix())

                .spare147(entity.getSpare147())
                .flightTransitLayover(entity.getFlightTransitLayover())
                .operatingAirlineDisclosure(entity.getOperatingAirlineDisclosure())
                .trafficRestrictionCode(entity.getTrafficRestrictionCode())
                .trafficRestrictionOverflow(entity.getTrafficRestrictionOverflow())
                .spare162To172(entity.getSpare162To172())

                .aircraftConfigurationVersion(entity.getAircraftConfigurationVersion())
                .dateVariation(entity.getDateVariation())

                .recordSerialNumber(entity.getRecordSerialNumber())

                .rawRecord(entity.getRawRecord())
                .parsedTimestamp(entity.getParsedTimestamp())

                .build();
    }

}
