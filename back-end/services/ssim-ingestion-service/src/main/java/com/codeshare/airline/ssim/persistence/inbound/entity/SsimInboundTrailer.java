package com.codeshare.airline.ssim.persistence.inbound.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "SSIM_INBOUND_TRAILER",
    schema = "SSIM_OPERATIONAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_SSIM_TRAILER_FILE",
            columnNames = "FILE_ID"
        )
    },
    indexes = {
        @Index(
            name = "IDX_SSIM_TRAILER_FILE",
            columnList = "FILE_ID"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundTrailer {

    /* ===================== IDENTITY ===================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAILER_ID")
    private Long id;

    /**
     * Owning inbound SSIM file
     * Exactly ONE T5 per SSIM file
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID", nullable = false, updatable = false)
    private SsimInboundFile file;

    /* ===================== SSIM FIELDS ===================== */

    /**
     * Byte 1
     * Record type – always '5'
     */
    @Column(name = "RECORD_TYPE", length = 1, nullable = false)
    private String recordType;

    /**
     * Bytes 2–36
     * Trailer title
     * Usually mirrors header title
     */
    @Column(name = "TRAILER_TITLE", length = 35, nullable = false)
    private String trailerTitle;

    /**
     * Bytes 37–45 (raw)
     * Total number of records as declared in trailer
     */
    @Column(name = "TOTAL_RECORD_COUNT_RAW", length = 9, nullable = false)
    private String totalRecordCountRaw;

    /**
     * Parsed total record count (derived)
     */
    @Column(name = "TOTAL_RECORD_COUNT")
    private Integer totalRecordCount;

    /**
     * Bytes 46–194
     * Spare / filler
     */
    @Column(name = "SPARE_46_194", length = 149, nullable = false)
    private String spare46To194;

    /**
     * Bytes 195–200
     * Record serial number
     */
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    /* ===================== RAW & AUDIT ===================== */

    /**
     * Full raw 200-byte SSIM line
     */
    @Column(name = "RAW_RECORD", length = 200, nullable = false)
    private String rawRecord;

    /**
     * Parsed timestamp
     */
    @Column(name = "PARSED_AT")
    private Instant parsedAt;

    /* ===================== VALIDATION SUPPORT ===================== */

    /**
     * Actual number of records processed for this file
     * (derived during ingestion)
     */
    @Column(name = "ACTUAL_RECORD_COUNT")
    private Integer actualRecordCount;

    /**
     * Whether declared trailer count matches actual records
     */
    @Column(name = "COUNT_MATCH")
    private Boolean countMatch;
}
