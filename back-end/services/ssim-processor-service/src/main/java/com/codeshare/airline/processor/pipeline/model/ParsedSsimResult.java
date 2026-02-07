package com.codeshare.airline.processor.parsing.model;

import com.codeshare.airline.processor.parsing.dto.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsedSsimResult {

    private final SsimRawFile rawFile;

    // Record-1 (exactly one)
    private R1HeaderDto r1Header;

    // Record-3
    private final List<R3FlightLegDto> r3FlightLegs = new ArrayList<>();

    // Record-4
    private final List<R4DateVariationDto> r4DateVariations = new ArrayList<>();

    // Record-5
    private final List<R5DateVariationDto> r5DateVariations = new ArrayList<>();

    // Appendix-H
    private final List<AppendixHCodeshareDto> appendixH = new ArrayList<>();

    // Unknown / unsupported
    private final List<String> unknownRecords = new ArrayList<>();

    public ParsedSsimResult(SsimRawFile rawFile) {
        this.rawFile = rawFile;
    }

    /* ---------- adders (DTOs only) ---------- */

    public void setR1Header(R1HeaderDto header) {
        this.r1Header = header;
    }

    public void addR3(R3FlightLegDto dto) {
        r3FlightLegs.add(dto);
    }

    public void addR4(R4DateVariationDto dto) {
        r4DateVariations.add(dto);
    }

    public void addR5(R5DateVariationDto dto) {
        r5DateVariations.add(dto);
    }

    public void addAppendixH(AppendixHCodeshareDto dto) {
        appendixH.add(dto);
    }

    public void addUnknown(String line) {
        unknownRecords.add(line);
    }
}
