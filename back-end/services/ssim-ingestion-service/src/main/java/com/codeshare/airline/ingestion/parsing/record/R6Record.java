package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * SSIM R6 – File Trailer / Footer Record
 * Informational only. No business logic.
 */
@Getter
@ToString
public class R6Record {

    /** Full 80-char raw SSIM line */
    private final String rawLine;

    /** Byte 1: Record type (always '6') */
    private final String recordType;

    /** Total number of records in file (raw) */
    private final String totalRecordCountRaw;

    /** Optional control checksum (raw) */
    private final String checksumRaw;

    public R6Record(String rawLine) {
        this.rawLine = rawLine;

        // Fixed-width parsing (positions illustrative)
        this.recordType           = rawLine.substring(0, 1);
        this.totalRecordCountRaw  = rawLine.substring(1, 7).trim();
        this.checksumRaw          = rawLine.substring(7, 15).trim();
    }
}
