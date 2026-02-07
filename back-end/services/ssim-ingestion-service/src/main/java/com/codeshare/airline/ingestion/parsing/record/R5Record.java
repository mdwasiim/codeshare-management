package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * SSIM R5 – Continuation / Date Variation Record
 * Modifies the immediately preceding R4 record.
 * Pure parsing model. No business logic.
 */
@Getter
@ToString
public class R5Record {

    /** Full 80-char raw SSIM line */
    private final String rawLine;

    /** Byte 1: Record type (always '5') */
    private final String recordType;

    /** Variation indicator (ADD / DELETE / MODIFY – raw) */
    private final String variationType;

    /** Variation start date (raw) */
    private final String dateFromRaw;

    /** Variation end date (raw) */
    private final String dateToRaw;

    /** Days of operation override (raw, optional) */
    private final String daysOverrideRaw;

    /** Departure time override (raw HHMM, optional) */
    private final String departureTimeOverrideRaw;

    /** Arrival time override (raw HHMM, optional) */
    private final String arrivalTimeOverrideRaw;

    public R5Record(String rawLine) {
        this.rawLine = rawLine;

        // Fixed-width parsing (positions illustrative – align with SSIM spec)
        this.recordType                 = rawLine.substring(0, 1);
        this.variationType              = rawLine.substring(1, 2).trim();
        this.dateFromRaw                = rawLine.substring(2, 8).trim();
        this.dateToRaw                  = rawLine.substring(8, 14).trim();
        this.daysOverrideRaw            = rawLine.substring(14, 21).trim();
        this.departureTimeOverrideRaw   = rawLine.substring(21, 25).trim();
        this.arrivalTimeOverrideRaw     = rawLine.substring(25, 29).trim();
    }
}
