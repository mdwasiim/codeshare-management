package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ComparisonStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * One comparison run — triggered when schedule-ingestion-service finishes
 * loading a message (SSIM file, SSM, or ASM) and publishes to Kafka.
 *
 * Tracks:
 *  - what was compared (ingested message reference + airline + source type)
 *  - when the run happened
 *  - outcome (COMPLETED / FAILED / SKIPPED)
 *  - summary counts of detected changes
 *
 * Each run produces zero or more {@link ScheduleFlightChangeEntity} children,
 * one per affected flight identity.
 */
@Entity
@Table(
        name = "schedule_comparison_run",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_scr_airline",     columnList = "airline_code"),
                @Index(name = "idx_scr_status",      columnList = "status"),
                @Index(name = "idx_scr_source_type", columnList = "source_type"),
                @Index(name = "idx_scr_ingested_ref",columnList = "ingested_message_id"),
                @Index(name = "idx_scr_file_ref",    columnList = "source_file_id"),
                @Index(name = "idx_scr_started_at",  columnList = "started_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleComparisonRunEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       SOURCE REFERENCE — the ingested message that triggered this run
       ========================================================== */

    // ID of the ScheduleMessageEntity (or SsimFileMetaDataEntity) in ingestion DB
    @Column(name = "ingested_message_id", nullable = false)
    private UUID ingestedMessageId;

    @Column(name = "source_file_id")
    private UUID sourceFileId;

    @Column(name = "source_load_id")
    private UUID sourceLoadId;

    // SSIM | SSM | ASM - which format triggered this run
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 10, nullable = false)
    private MessageType sourceType;

    // The airline this run covers
    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    // Human-readable reference from the message (e.g. SSM message reference)
    @Column(name = "message_reference", length = 50)
    private String messageReference;

    @Column(name = "source_system", length = 50)
    private String sourceSystem;

    @Column(name = "source_file_name", length = 255)
    private String sourceFileName;

    @Column(name = "source_checksum", length = 128)
    private String sourceChecksum;

    @Column(name = "source_creation_date_raw", length = 20)
    private String sourceCreationDateRaw;

    @Column(name = "source_creation_time_raw", length = 20)
    private String sourceCreationTimeRaw;

    @Column(name = "source_created_at")
    private Instant sourceCreatedAt;

    @Column(name = "source_received_at")
    private Instant sourceReceivedAt;

    /* ==========================================================
       RUN TIMING
       ========================================================== */

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    /* ==========================================================
       OUTCOME
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ComparisonStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /* ==========================================================
       SUMMARY COUNTS  (denormalised for dashboard/reporting)
       ========================================================== */

    @Column(name = "total_legs_compared")
    private Integer totalLegsCompared;

    @Column(name = "new_count")
    private Integer newCount;

    @Column(name = "cancelled_count")
    private Integer cancelledCount;

    @Column(name = "retimed_count")
    private Integer retimedCount;

    @Column(name = "equipment_count")
    private Integer equipmentCount;

    @Column(name = "other_change_count")
    private Integer otherChangeCount;

    @Column(name = "no_change_count")
    private Integer noChangeCount;

    /* ==========================================================
       CHILDREN
       ========================================================== */

    @OneToMany(
            mappedBy = "comparisonRun",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("airlineCode ASC, flightNumber ASC")
    private List<ScheduleFlightChangeEntity> flightChanges = new ArrayList<>();

    /* ==========================================================
       HELPERS
       ========================================================== */

    public void addFlightChange(ScheduleFlightChangeEntity flightChange) {
        if (flightChange != null) {
            flightChanges.add(flightChange);
            flightChange.setComparisonRun(this);
        }
    }
}
