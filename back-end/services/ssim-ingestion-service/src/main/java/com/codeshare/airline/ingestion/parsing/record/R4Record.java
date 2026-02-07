package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * SSIM R4 – Core Flight Schedule Record
 * Pure parsing model. No business logic.
 */
@Getter
@ToString
public class R4Record {

    /** Full 80-char raw SSIM line */
    private final String rawLine;

    /** Byte 1: Record type (always '4') */
    private final String recordType;

    /** Operating carrier code */
    private final String carrierCode;

    /** Flight number */
    private final String flightNumber;

    /** Origin airport (IATA) */
    private final String origin;

    /** Destination airport (IATA) */
    private final String destination;

    /** Days of operation (raw, e.g. 1234567) */
    private final String daysOfOperationRaw;

    /** Departure time (raw HHMM) */
    private final String departureTimeRaw;

    /** Arrival time (raw HHMM) */
    private final String arrivalTimeRaw;

    /** Aircraft type */
    private final String aircraftType;

    /** Service type (P/C/etc.) */
    private final String serviceType;

    public R4Record(String rawLine) {
        this.rawLine = rawLine;

        // Fixed-width parsing (positions illustrative – align with SSIM spec)
        this.recordType          = rawLine.substring(0, 1);
        this.carrierCode         = rawLine.substring(1, 4).trim();
        this.flightNumber        = rawLine.substring(4, 8).trim();
        this.origin              = rawLine.substring(8, 11).trim();
        this.destination         = rawLine.substring(11, 14).trim();
        this.daysOfOperationRaw  = rawLine.substring(14, 21).trim();
        this.departureTimeRaw    = rawLine.substring(21, 25).trim();
        this.arrivalTimeRaw      = rawLine.substring(25, 29).trim();
        this.aircraftType        = rawLine.substring(29, 32).trim();
        this.serviceType         = rawLine.substring(32, 33).trim();
    }
}
