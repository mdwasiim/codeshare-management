package com.codeshare.airline.ssim.persistence.inbound.entity;

import com.codeshare.airline.ssim.source.SsimProcessingStatus;
import com.codeshare.airline.ssim.source.SsimProfile;
import com.codeshare.airline.ssim.source.SsimSourceType;
import com.codeshare.airline.ssim.source.SsimTimeMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "SSIM_INBOUND_FILE",
    indexes = {
        @Index(name = "IDX_SSIM_FILE_AIRLINE", columnList = "AIRLINE_CODE"),
        @Index(name = "IDX_SSIM_FILE_RECEIVED_AT", columnList = "RECEIVED_AT"),
        @Index(name = "IDX_SSIM_FILE_STATUS", columnList = "PROCESSING_STATUS"),
        @Index(name = "IDX_SSIM_FILE_PROFILE", columnList = "SSIM_PROFILE"),
        @Index(name = "IDX_SSIM_FILE_CHECKSUM", columnList = "AIRLINE_CODE,CHECKSUM"),
        @Index(name = "IDX_SSIM_FILE_LOAD", columnList = "LOAD_ID")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundFile {

    /* ===================== IDENTITY ===================== */

    /**
     * System-generated unique identifier (UUID)
     * Primary key for this inbound SSIM file
     */
    @Id
    @Column(name = "FILE_ID", length = 36)
    private String fileId;

    /**
     * Correlation ID for a single ingestion run
     * Same LOAD_ID can span multiple files (batch ingestion)
     */
    @Column(name = "LOAD_ID", length = 36)
    private String loadId;

    /**
     * Optional external reference
     * (Kafka message key, SFTP batch ID, API request ID)
     */
    @Column(name = "EXTERNAL_REFERENCE", length = 64)
    private String externalReference;

    /* ===================== OWNERSHIP ===================== */

    /**
     * Primary airline owning the SSIM file
     */
    @Column(name = "AIRLINE_CODE", length = 3)
    private String airlineCode;

    /**
     * Airline name (informational, from T2 if present)
     */
    @Column(name = "AIRLINE_NAME", length = 50)
    private String airlineName;

    /* ===================== FILE ORIGIN ===================== */

    /**
     * Original file name as received
     */
    @Column(name = "FILE_NAME", length = 255, nullable = false)
    private String fileName;

    /**
     * Source channel of the file
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE_TYPE", length = 20, nullable = false)
    private SsimSourceType sourceType;

    /**
     * Source system / partner identifier
     * (SFTP host, GDS name, API client, etc.)
     */
    @Column(name = "SOURCE_SYSTEM", length = 50)
    private String sourceSystem;

    /* ===================== FILE CHARACTERISTICS ===================== */

    /**
     * File size in bytes
     */
    @Column(name = "FILE_SIZE_BYTES")
    private Long fileSizeBytes;

    /**
     * Total number of physical records (lines)
     */
    @Column(name = "TOTAL_RECORDS")
    private Integer totalRecords;

    /**
     * File checksum (SHA-256 recommended)
     */
    @Column(name = "CHECKSUM", length = 64)
    private String checksum;

    /**
     * Character encoding (usually ASCII)
     */
    @Column(name = "CHARACTER_SET", length = 20)
    private String characterSet;

    /* ===================== SSIM CLASSIFICATION ===================== */

    /**
     * SSIM profile detected for this file
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SSIM_PROFILE", length = 20, nullable = false)
    private SsimProfile ssimProfile;

    /**
     * SSIM specification version (e.g. 7, 8)
     */
    @Column(name = "SSIM_VERSION", length = 10)
    private String ssimVersion;

    /**
     * Time mode derived from T2 carrier record
     * U = UTC, L = Local
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIME_MODE", length = 1)
    private SsimTimeMode timeMode;

    /* ===================== PROCESSING STATUS ===================== */

    /**
     * Current processing status of this file
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PROCESSING_STATUS", length = 20, nullable = false)
    private SsimProcessingStatus processingStatus;

    /**
     * Whether this file supersedes a previous SSIM file
     */
    @Column(name = "IS_SUPERSEDING")
    private Boolean superseding;

    /**
     * File ID of the superseded file (if applicable)
     */
    @Column(name = "SUPERSEDES_FILE_ID", length = 36)
    private String supersedesFileId;

    /* ===================== TIMESTAMPS ===================== */

    /**
     * When the file was received by the platform
     */
    @Column(name = "RECEIVED_AT", nullable = false)
    private Instant receivedAt;

    /**
     * When parsing started
     */
    @Column(name = "PARSED_AT")
    private Instant parsedAt;

    /**
     * When validation completed
     */
    @Column(name = "VALIDATED_AT")
    private Instant validatedAt;

    /**
     * When persistence completed
     */
    @Column(name = "STORED_AT")
    private Instant storedAt;

    /**
     * When processing failed (if applicable)
     */
    @Column(name = "FAILED_AT")
    private Instant failedAt;

    /* ===================== ERROR HANDLING ===================== */

    /**
     * Error code (functional or technical)
     */
    @Column(name = "ERROR_CODE", length = 50)
    private String errorCode;

    /**
     * Error description
     */
    @Column(name = "ERROR_MESSAGE", length = 500)
    private String errorMessage;

    /* ===================== AUDIT ===================== */

    /**
     * Who triggered ingestion
     * (SYSTEM, USER_ID, API_CLIENT)
     */
    @Column(name = "INGESTED_BY", length = 50)
    private String ingestedBy;

    /**
     * Free-form operational remarks
     */
    @Column(name = "REMARKS", length = 1000)
    private String remarks;
}
