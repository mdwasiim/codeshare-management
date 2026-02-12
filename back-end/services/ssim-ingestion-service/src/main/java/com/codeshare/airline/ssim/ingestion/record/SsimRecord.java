package com.codeshare.airline.ssim.ingestion.record;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SsimRecord {

    private final char recordType;
    private final String rawLine;

    public SsimRecord(String rawLine) {
        this.rawLine = rawLine;
        this.recordType = rawLine.charAt(0);
    }
}
