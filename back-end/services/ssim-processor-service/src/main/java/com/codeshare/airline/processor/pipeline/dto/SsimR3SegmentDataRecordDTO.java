package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimR3SegmentDataRecordDTO extends CSMAuditableDTO {

    private Long id;

    // FK reference (DTO-safe)
    private Long flightLegId;

    // System-only ordering (not SSIM)
    private Integer sequenceNumber;

    // SSIM Record-4 fields
    private String boardPoint;                 // Bytes 10–12
    private String offPoint;                   // Bytes 13–15
    private String dataElementIdentifier;      // Bytes 16–18
    private String dataElementValue;           // Bytes 19–78 (60 chars)
}
