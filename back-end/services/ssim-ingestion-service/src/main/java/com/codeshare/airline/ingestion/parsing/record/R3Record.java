package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * SSIM R3 – Traffic Restriction / Operational Qualifier Record
 * Pure parsing model. No business logic.
 */
@Getter
@ToString
public class R3Record {

    /** Full 80-char raw SSIM line */
    private final String rawLine;

    /** Byte 1: Record type (always '3') */
    private final String recordType;

    /** Restriction or traffic right code */
    private final String restrictionCode;

    /** Additional qualifier */
    private final String qualifier;

    public R3Record(String rawLine) {
        this.rawLine = rawLine;

        // Fixed-width parsing (positions illustrative)
        this.recordType      = rawLine.substring(0, 1);
        this.restrictionCode = rawLine.substring(1, 3).trim();
        this.qualifier       = rawLine.substring(3, 6).trim();
    }
}
