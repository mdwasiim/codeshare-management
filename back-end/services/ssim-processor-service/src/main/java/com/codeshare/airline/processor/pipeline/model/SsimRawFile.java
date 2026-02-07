package com.codeshare.airline.processor.pipeline.model;

import com.codeshare.airline.core.enums.SsimSourceType;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SsimRawFile {

    /** Unique ingestion tracking ID */
    private UUID ingestionId;

    /** Local / SFTP / Email / Cloud */
    private SsimSourceType sourceType;

    /** Original file name */
    private String originalFileName;

    /** When ingestion picked it up */
    private Instant receivedAt;

    // -------- Minimal R1 extraction --------
    private String airlineCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    /** Canonical dedup fingerprint (SHA-256 of content) */
    private String fingerprint;

    /** Raw SSIM bytes (optional but recommended for replay) */
    private byte[] content;

    /** Parsed lines (optional, convenience for processor) */
    private List<SsimRawLine> lines;

}
