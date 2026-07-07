package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleSource;
import com.codeshare.airline.schedule.live.domain.enums.ScheduleChangeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable version/audit snapshot of a {@link LiveFlightLegEntity}.
 *
 * Every time a live leg is created or updated (from a delta produced by
 * schedule-processing-service), a new version row is appended here.
 * The full T3 raw line is preserved so the exact SSIM/SSM/ASM state
 * that triggered the change can be reproduced at any point in time.
 *
 * Supports:
 *  - Full change history for regulatory audit trails.
 *  - Rollback to a prior state when a change is reversed.
 *  - Comparison input for the schedule-processing-service delta engine.
 */
@Entity
@Table(
        name = "live_schedule_version",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lsv_leg",        columnList = "flight_leg_id"),
                @Index(name = "idx_lsv_change",     columnList = "change_type"),
                @Index(name = "idx_lsv_source",     columnList = "source, source_reference_id"),
                @Index(name = "idx_lsv_applied_at", columnList = "applied_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LiveScheduleVersionEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_leg_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_lsv_flight_leg")
    )
    private LiveFlightLegEntity flightLeg;

    /* ==========================================================
       VERSION IDENTITY
       ========================================================== */

    @Column(name = "version_number", nullable = false)
    private Long versionNumber;

    /* ==========================================================
       CHANGE CLASSIFICATION
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", length = 20, nullable = false)
    private ScheduleChangeType changeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 10, nullable = false)
    private LiveScheduleSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 10)
    private MessageType messageType;            // SSIM, SSM, or ASM

    /* ==========================================================
       SOURCE TRACEABILITY
       ========================================================== */

    @Column(name = "source_reference_id")
    private UUID sourceReferenceId;             // processing-service delta record ID

    @Column(name = "source_file_id")
    private UUID sourceFileId;                  // ingestion file that triggered the change

    @Column(name = "source_load_id")
    private UUID sourceLoadId;

    /* ==========================================================
       SNAPSHOT  (full T3 raw line preserved for audit)
       ========================================================== */

    @Column(name = "raw_t3_line", length = 200)
    private String rawT3Line;                   // original 200-byte SSIM Record Type 3

    /* ==========================================================
       FIELD-LEVEL CHANGE SUMMARY  (JSON diff stored as text)
       ========================================================== */

    @Column(name = "change_summary", columnDefinition = "TEXT")
    private String changeSummary;               // JSON: {"field": ["oldVal", "newVal"], …}

    /* ==========================================================
       TIMING
       ========================================================== */

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    @Column(name = "applied_by", length = 100)
    private String appliedBy;

    /* ==========================================================
       PERIOD SNAPSHOT  (duplicated from leg for point-in-time queries)
       ========================================================== */

    @Column(name = "snapshot_period_start_raw", length = 7)
    private String snapshotPeriodStartRaw;

    @Column(name = "snapshot_period_end_raw", length = 7)
    private String snapshotPeriodEndRaw;

    @Column(name = "snapshot_days_of_operation", length = 7)
    private String snapshotDaysOfOperation;

    @Column(name = "snapshot_departure_station", length = 3)
    private String snapshotDepartureStation;

    @Column(name = "snapshot_arrival_station", length = 3)
    private String snapshotArrivalStation;

    @Column(name = "snapshot_aircraft_std_raw", length = 4)
    private String snapshotAircraftStdRaw;

    @Column(name = "snapshot_aircraft_sta_raw", length = 4)
    private String snapshotAircraftStaRaw;

    @Column(name = "snapshot_aircraft_type", length = 3)
    private String snapshotAircraftType;
}
