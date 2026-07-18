package com.codeshare.airline.schedule.ingestion.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SsimFlightSearchCriteria {

    private UUID fileId;
    private String recordType;
    private String operationalSuffix;
    private String airlineCode;
    private String flightNumber;
    private String itineraryVariationIdentifier;
    private Integer legSequenceNumber;
    private String serviceType;
    private String operatingPeriodStartRaw;
    private String operatingPeriodEndRaw;
    private String operatingDays;
    private String frequencyRate;
    private String departureStation;
    private String passengerStd;
    private String aircraftStd;
    private String arrivalStation;
    private String aircraftSta;
    private String passengerSta;
    private String arrivalTerminal;
    private String aircraftType;
    private String passengerReservationBookingDesignator;
    private String recordSerialNumber;
}
