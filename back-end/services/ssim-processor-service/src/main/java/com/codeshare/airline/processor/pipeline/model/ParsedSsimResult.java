package com.codeshare.airline.processor.pipeline.model;

import com.codeshare.airline.processor.pipeline.dto.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsedSsimResult {

    private final SsimRawFile rawFile;

    /* =========================================================
     * ORDERED RECORD STREAM (AUTHORITATIVE)
     * ========================================================= */

    /**
     * Maintains exact SSIM file order.
     * Elements are DTOs: R1, R2, R3, R4, R5, Appendix-H.
     */
    private final List<Object> orderedRecords = new ArrayList<>();

    /* =========================================================
     * TYPED ACCESS (CONVENIENCE)
     * ========================================================= */

    // Record 1 – Header (exactly one)
    private SsimR1HeaderRecordDTO header;

    // Record 2 – Carrier
    private final List<SsimR2CarrierRecordDTO> carriers = new ArrayList<>();

    // Record 3 – Flight Leg
    private final List<SsimR3FlightLegRecordDTO> flightLegs = new ArrayList<>();

    // Record 4 – Segment Data (DEIs)
    private final List<SsimR3SegmentDataRecordDTO> segmentData = new ArrayList<>();

    // Record 5 – Date Variation
    private final List<SsimR4DateVariationRecordDTO> dateVariations = new ArrayList<>();

    // Record 6 – Continuation
    private final List<SsimR5ContinuationRecordDTO> continuations = new ArrayList<>();

    private final List<SsimAppendixHCodeshareDTO> codeshares = new ArrayList<>();

    /* =========================================================
     * DIAGNOSTICS
     * ========================================================= */

    private final List<String> structuralErrors = new ArrayList<>();
    private final List<String> structuralWarnings = new ArrayList<>();
    private final List<String> unknownRecords = new ArrayList<>();

    public ParsedSsimResult(SsimRawFile rawFile) {
        this.rawFile = rawFile;
    }

    /* =========================================================
     * ADDERS (ALWAYS ADD TO orderedRecords)
     * ========================================================= */

    public void setR1Header(SsimR1HeaderRecordDTO header) {
        this.header = header;
        orderedRecords.add(header);
    }

    public void addR2(SsimR2CarrierRecordDTO dto) {
        carriers.add(dto);
        orderedRecords.add(dto);
    }

    public void addR3(SsimR3FlightLegRecordDTO dto) {
        flightLegs.add(dto);
        orderedRecords.add(dto);
    }

    public void addR3Segment(SsimR3SegmentDataRecordDTO dto) {
        segmentData.add(dto);
        orderedRecords.add(dto);
    }

    public void addR4(SsimR4DateVariationRecordDTO dto) {
        dateVariations.add(dto);
        orderedRecords.add(dto);
    }

    public void addR5(SsimR5ContinuationRecordDTO dto) {
        continuations.add(dto);
        orderedRecords.add(dto);
    }

    public void addError(String error) {
        structuralErrors.add(error);
    }

    public void addWarning(String warning) {
        structuralWarnings.add(warning);
    }

    public void addUnknown(String line) {
        unknownRecords.add(line);
    }
}
