package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.Getter;

import java.time.Instant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimInboundDatasetMetadataDTO extends CSMAuditableDTO {

    private Long id;

    private String datasetSerialNumber;
    private String seasonCode;

    private String fileName;
    private String sourceSystem;
    private String publishingCarrier;
    private String sourceChannel;

    private Instant receivedAt;
    private Instant loadedAt;
    private Instant processedAt;
    private String datasetStatus;

    private String checksum;

    private int totalCarriers;
    private int totalFlightLegs;
    private int totalSegmentRecords;
    private int totalDateVariations;
    private int totalContinuationRecords;

    private boolean structurallyValid;
    private boolean appendixHValid;

    private String validationReport;
}
