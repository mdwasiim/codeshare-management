package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * SSIM R2 – Carrier / Service Information Record
 * Pure parsing model. No business logic.
 */
@Getter
@ToString
public class R2Record {

    /** Full 80-char raw SSIM line */
    private final String rawLine;

    /** Byte 1: Record type (always '2') */
    private final String recordType;

    /** Carrier designator (marketing / operating) */
    private final String carrierCode;

    /** Service type (P = Passenger, C = Cargo, etc.) */
    private final String serviceType;

    /** Traffic restriction indicator */
    private final String trafficRestriction;

    public R2Record(String rawLine) {
        this.rawLine = rawLine;

        // Fixed-width parsing (positions are illustrative)
        this.recordType         = rawLine.substring(0, 1);
        this.carrierCode        = rawLine.substring(1, 4).trim();
        this.serviceType        = rawLine.substring(4, 5).trim();
        this.trafficRestriction = rawLine.substring(5, 6).trim();
    }
}
