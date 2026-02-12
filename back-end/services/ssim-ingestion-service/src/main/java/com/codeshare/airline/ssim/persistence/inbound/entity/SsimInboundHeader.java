package com.codeshare.airline.ssim.persistence.inbound.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "SSIM_INBOUND_HEADER",
    schema = "SSIM_OPERATIONAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_SSIM_HEADER_FILE",
            columnNames = "FILE_ID"
        )
    },
    indexes = {
        @Index(
            name = "IDX_SSIM_HEADER_FILE",
            columnList = "FILE_ID"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundHeader {

    /* ===================== IDENTITY ===================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HEADER_ID")
    private Long id;

    /**
     * Owning inbound SSIM file
     * Exactly ONE T1 per SSIM file
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID", nullable = false, updatable = false)
    private SsimInboundFile file;

    /* ===================== SSIM FIELDS ===================== */

    /**
     * Byte 1
     * Record type – always '1'
     */
    @Column(name = "RECORD_TYPE", length = 1, nullable = false)
    private String recordType;

    /**
     * Bytes 2–36 (35)
     * Title of contents
     * Usually: "AIRLINE STANDARD SCHEDULE DATASET"
     */
    @Column(name = "TITLE_OF_CONTENTS", length = 35, nullable = false)
    private String titleOfContents;

    /**
     * Bytes 37–39 (3)
     * Dataset serial number
     */
    @Column(name = "DATASET_SERIAL_NUMBER", length = 3)
    private String datasetSerialNumber;

    /**
     * Bytes 40–45 (6)
     * Record serial number (normally 000001)
     */
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    /**
     * Bytes 46–200 (155)
     * Spare / filler
     */
    @Column(name = "SPARE_46_200", length = 155, nullable = false)
    private String spare46To200;

    /**
     * Full raw 200-byte SSIM line (immutable)
     */
    @Column(name = "RAW_RECORD", length = 200, nullable = false)
    private String rawRecord;

    /* ===================== AUDIT ===================== */

    /**
     * Parsed timestamp (for traceability/debug)
     */
    @Column(name = "PARSED_AT")
    private Instant parsedAt;
}
