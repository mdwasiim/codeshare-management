package com.codeshare.airline.ingestion.parsing.record;

import lombok.Getter;
import lombok.ToString;

/**
 * Transport wrapper for a parsed SSIM record.
 * Immutable and schema-stable.
 */
@Getter
@ToString
public class SsimRecord {

    /** Record type: '1'..'6' */
    private final char recordType;

    /** Full 80-character SSIM line */
    private final String rawLine;

    /** Parsed payload (R1Record, R4Record, etc.) */
    private final Object payload;

    public SsimRecord(char recordType, String rawLine, Object payload) {
        this.recordType = recordType;
        this.rawLine = rawLine;
        this.payload = payload;
    }

    /**
     * Safe typed access to payload.
     */
    @SuppressWarnings("unchecked")
    public <T> T payload(Class<T> type) {
        return (T) payload;
    }
}
