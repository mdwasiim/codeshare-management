package com.codeshare.airline.ssim.inbound.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimTimeMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "SSIM_INBOUND_FILE",
        schema = "SSIM_OPERATIONAL",
        indexes = {
                @Index(name = "IDX_SSIM_IN_FILE_AIRLINE", columnList = "AIRLINE_CODE"),
                @Index(name = "IDX_SSIM_IN_FILE_RECEIVED_AT", columnList = "RECEIVED_TIMESTAMP"),
                @Index(name = "IDX_SSIM_IN_FILE_STATUS", columnList = "PROCESSING_STATUS"),
                @Index(name = "IDX_SSIM_IN_FILE_PROFILE", columnList = "SSIM_PROFILE"),
                @Index(name = "IDX_SSIM_IN_FILE_LOAD_ID", columnList = "LOAD_ID"),
                @Index(name = "IDX_SSIM_IN_FILE_AIRLINE_CHECKSUM", columnList = "AIRLINE_CODE,CHECKSUM")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_SSIM_IN_FILE_AIRLINE_CHECKSUM",
                        columnNames = {"AIRLINE_CODE", "CHECKSUM"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundFile extends CSMDataAbstractEntity {


    @OneToOne(
            mappedBy = "inboundFile",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private SsimInboundCarrier carrier;

    /* =======================================================
       INGESTION CORRELATION
       ======================================================= */

    /**
     * Correlation ID for one ingestion run
     */
    @Column(name = "LOAD_ID", length = 36)
    private UUID loadId;

    /**
     * External reference (Kafka key, SFTP batch ID, API request ID)
     */
    @Column(name = "EXTERNAL_REFERENCE", length = 64)
    private String externalReference;


    /* =======================================================
       AIRLINE OWNERSHIP
       ======================================================= */

    @Column(name = "AIRLINE_CODE", length = 3)
    private String airlineCode;

    @Column(name = "AIRLINE_NAME", length = 50)
    private String airlineName;


    /* =======================================================
       FILE ORIGIN
       ======================================================= */

    @Column(name = "FILE_NAME", length = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE_TYPE", length = 20)
    private SsimSourceType sourceType;

    @Column(name = "SOURCE_SYSTEM", length = 50)
    private String sourceSystem;


    /* =======================================================
       FILE CHARACTERISTICS
       ======================================================= */

    @Column(name = "FILE_SIZE_BYTES")
    private Long fileSizeBytes;

    @Column(name = "TOTAL_RECORD_COUNT")
    private Integer totalRecordCount;

    /**
     * SHA-256 recommended
     */
    @Column(name = "CHECKSUM", length = 64)
    private String checksum;

    @Column(name = "CHARACTER_SET", length = 20)
    private String characterSet;


    /* =======================================================
       SSIM CLASSIFICATION
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "SSIM_PROFILE", length = 20)
    private SsimProfile ssimProfile;

    @Column(name = "SSIM_VERSION", length = 10)
    private String ssimVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIME_MODE", length = 1)
    private SsimTimeMode timeMode;


    /* =======================================================
       PROCESSING STATE
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "PROCESSING_STATUS", length = 20)
    private SsimProcessingStatus processingStatus;

    @Column(name = "IS_SUPERSEDING")
    private Boolean isSuperseding = Boolean.FALSE;

    /**
     * Logical self-reference (optional)
     */
    @Column(name = "SUPERSEDED_FILE_ID", length = 36)
    private String supersededFileId;


    /* =======================================================
       PROCESSING TIMESTAMPS
       ======================================================= */

    @Column(name = "RECEIVED_TIMESTAMP", updatable = false)
    private Instant receivedTimestamp;

    @Column(name = "PARSED_TIMESTAMP")
    private Instant parsedTimestamp;

    @Column(name = "VALIDATED_TIMESTAMP")
    private Instant validatedTimestamp;

    @Column(name = "STORED_TIMESTAMP")
    private Instant storedTimestamp;

    @Column(name = "FAILED_TIMESTAMP")
    private Instant failedTimestamp;


    /* =======================================================
       ERROR HANDLING
       ======================================================= */

    @Column(name = "ERROR_CODE", length = 50)
    private String errorCode;

    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;


    /* =======================================================
       AUDIT / OPERATIONAL
       ======================================================= */

    @Column(name = "INGESTED_BY", length = 50)
    private String ingestedBy;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;
}
