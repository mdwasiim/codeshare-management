package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * SSIM R1 – File Header Record
 * Pure parsing model. No business logic.
 */
@Getter
@ToString
public class R1Record {

    /** Full 80-char raw SSIM line */
    private final String rawLine;

    /** Byte 1: Record type (always '1') */
    private final String recordType;

    /** Airline designator */
    private final String airlineCode;

    /** IATA season code (S / W) */
    private final String season;

    /** Schedule validity start (raw YYMMDD or DDMMYY depending on spec) */
    private final String validityStartRaw;

    /** Schedule validity end (raw) */
    private final String validityEndRaw;

    /** File creation date (raw) */
    private final String creationDateRaw;

    public R1Record(String rawLine) {
        this.rawLine = rawLine;

        // Fixed-width parsing (positions are illustrative)
        this.recordType        = rawLine.substring(0, 1);
        this.airlineCode       = rawLine.substring(1, 4).trim();
        this.season            = rawLine.substring(4, 5).trim();
        this.validityStartRaw  = rawLine.substring(5, 11).trim();
        this.validityEndRaw    = rawLine.substring(11, 17).trim();
        this.creationDateRaw   = rawLine.substring(17, 23).trim();
    }
}
