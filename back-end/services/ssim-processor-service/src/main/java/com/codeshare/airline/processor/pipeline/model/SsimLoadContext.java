package com.codeshare.airline.processor.pipeline.model;

import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import com.codeshare.airline.processor.pipeline.dto.*;
import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class SsimLoadContext {

    /* ===================== DATASET ===================== */

    private final SsimInboundDatasetMetadataDTO datasetMetadata;

    /* ===================== SSIM RECORDS ===================== */

    private final SsimR1HeaderRecord header;
    private final List<SsimR2CarrierRecordDTO> carriers;
    private final List<SsimR3FlightLegRecordDTO> flightLegs;
    private final List<SsimR4DateVariationRecordDTO> dateVariations;
    private final List<SsimR5ContinuationRecordDTO> continuations;

    @Builder.Default
    private final List<SsimAppendixHCodeshareDTO> codeshares = new ArrayList<>();

    /* ===================== PROCESSING STATE ===================== */

    private final boolean structuralValid;
    private final boolean appendixHValid;

    private final List<String> validationErrors;
    private final List<String> validationWarnings;

    /* ===================== STATS ===================== */

    private final int totalCarriers;
    private final int totalFlightLegs;
    private final int totalSegmentRecords;
    private final int totalDateVariations;
    private final int totalContinuationRecords;

    /* ===================== RAW / TRACEABILITY ===================== */

    private final String sourceFileName;
    private final String sourceChecksum;

    private final Map<Integer, String> rawLinesByLineNumber;

    /* ===================== LIFECYCLE ===================== */

    private final UUID datasetId;
    private final Instant loadedAt;
    private final Instant validatedAt;
    private final Instant expandedAt;
    private final Instant completedAt;

    private final SsimLoadStatus status;
    private final List<String> errors;
    private final List<String> warnings;
}
