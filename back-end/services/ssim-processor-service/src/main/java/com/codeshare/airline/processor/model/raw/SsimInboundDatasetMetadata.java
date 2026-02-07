package com.codeshare.airline.processor.entities.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(
        name = "ssim_inbound_dataset",
        indexes = {
                @Index(
                        name = "idx_ssim_dataset_serial",
                        columnList = "dataset_serial_number"
                ),
                @Index(
                        name = "idx_ssim_dataset_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_ssim_dataset_carrier",
                        columnList = "publishing_carrier"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimInboundDatasetMetadata extends CSMDataAbstractEntity {

    /* =========================================================
     * IDENTITY
     * ========================================================= */

    /**
     * Dataset serial number from SSIM Record-1.
     * Example: 000001
     */
    @Column(
            name = "dataset_serial_number",
            nullable = false,
            unique = true,
            length = 6
    )
    private String datasetSerialNumber;

    /**
     * Derived season code (e.g. S26, W26).
     */
    @Column(name = "season_code", length = 4)
    private String seasonCode;

    /* =========================================================
     * SOURCE INFORMATION
     * ========================================================= */

    /**
     * Original inbound file name.
     */
    @Column(name = "file_name", length = 255)
    private String fileName;

    /**
     * Source system providing SSIM (OAG, SITA, AIRLINE, etc.).
     */
    @Column(name = "source_system", length = 20)
    private String sourceSystem;

    /**
     * Publishing carrier (usually same as Record-1 airline).
     */
    @Column(name = "publishing_carrier", length = 3)
    private String publishingCarrier;

    /**
     * Ingestion channel (SFTP, API, UI).
     */
    @Column(name = "source_channel", length = 20)
    private String sourceChannel;

    /* =========================================================
     * LIFECYCLE TIMESTAMPS
     * ========================================================= */

    /**
     * When the file was received by the system.
     */
    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    /**
     * When SSIM records were persisted.
     */
    @Column(name = "loaded_at", nullable = false)
    private Instant loadedAt;

    /**
     * When business processing started.
     */
    @Column(name = "processed_at")
    private Instant processedAt;

    /**
     * Dataset serial that this dataset supersedes (if any).
     */
    @Column(name = "supersedes_dataset_serial", length = 6)
    private String supersedesDatasetSerial;

    /* =========================================================
     * INTEGRITY
     * ========================================================= */

    /**
     * SHA-256 checksum of the raw SSIM file.
     */
    @Column(name = "checksum", length = 64)
    private String checksum;

    /* =========================================================
     * COUNTS / STATISTICS
     * ========================================================= */

    @Column(name = "total_carriers")
    private int totalCarriers;

    @Column(name = "total_flight_legs")
    private int totalFlightLegs;

    @Column(name = "total_segment_records")
    private int totalSegmentRecords;

    @Column(name = "total_date_variations")
    private int totalDateVariations;

    @Column(name = "total_continuation_records")
    private int totalContinuationRecords;

    /* =========================================================
     * VALIDATION
     * ========================================================= */

    /**
     * Structural SSIM validation passed.
     */
    @Column(name = "is_structurally_valid", nullable = false)
    private boolean structurallyValid = false;

    /**
     * Appendix-H codeshare validation passed.
     */
    @Column(name = "is_appendix_h_valid", nullable = false)
    private boolean appendixHValid = false;

    /**
     * Detailed validation output (human-readable).
     */
    @Lob
    @Column(name = "validation_report")
    private String validationReport;

    /* =========================================================
     * STATUS & PHASE TRACKING
     * ========================================================= */

    /**
     * Current dataset lifecycle status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SsimLoadStatus status;

    @Column(name = "validated_at")
    private Instant validatedAt;

    @Column(name = "expanded_at")
    private Instant expandedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    /**
     * Error summary (JSON or text) in case of failure.
     */
    @Column(name = "error_summary", length = 4000)
    private String errorSummary;
}


