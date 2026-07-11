package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.MergeStatus;
import com.codeshare.airline.schedule.processing.domain.enums.SegmentChangeType;
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
 * Comparison result for one board/off-point segment on a flight leg.
 *
 * A leg may have many segments (e.g. DXB-LHR, DXB-MAN on the same leg).
 * Each segment is compared independently between the ingested data and
 * the live schedule. This entity records whether the segment was added,
 * removed, or whether only its DEI data changed.
 *
 * Scoped to a {@link ScheduleLegChangeEntity} which defines the leg,
 * period, and SSM action type.
 *
 * Cross-service UUIDs:
 *   - {@code liveSegmentId}     â€” LiveSegmentEntity.id in schedule-service (null if ADDED)
 *   - {@code ingestedSegmentId} â€” ScheduleDataElementEntity / segment ref in ingestion-service (null if REMOVED)
 *
 * DEI-level changes on this segment are stored in {@link ScheduleDeiChangeEntity} children.
 */
@Entity
@Table(
        name = "schedule_segment_change",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_ssc_leg_change",   columnList = "leg_change_id"),
                @Index(name = "idx_ssc_segment",      columnList = "board_point, off_point"),
                @Index(name = "idx_ssc_change_type",  columnList = "segment_change_type"),
                @Index(name = "idx_ssc_merge_status", columnList = "merge_status"),
                @Index(name = "idx_ssc_live_seg",     columnList = "live_segment_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ssc_leg_segment",
                        columnNames = {"leg_change_id", "board_point", "off_point"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleSegmentChangeEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP TO LEG CHANGE
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "leg_change_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_ssc_leg_change")
    )
    private ScheduleLegChangeEntity legChange;

    /* ==========================================================
       SEGMENT IDENTITY
       ========================================================== */

    @Column(name = "board_point", length = 3, nullable = false)
    private String boardPoint;

    @Column(name = "off_point", length = 3, nullable = false)
    private String offPoint;

    /* ==========================================================
       CHANGE CLASSIFICATION
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "segment_change_type", length = 15, nullable = false)
    private SegmentChangeType segmentChangeType;

    /* ==========================================================
       CROSS-SERVICE REFERENCES  (UUID only â€” different databases)
       ========================================================== */

    // LiveSegmentEntity.id in schedule-service â€” null if ADDED
    @Column(name = "live_segment_id")
    private UUID liveSegmentId;

    // Reference to segment data in schedule-ingestion-service â€” null if REMOVED
    @Column(name = "ingested_segment_id")
    private UUID ingestedSegmentId;

    /* ==========================================================
       MERGE TRACKING
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "merge_status", length = 20, nullable = false)
    private MergeStatus mergeStatus;

    @Column(name = "merged_at")
    private Instant mergedAt;

    @Column(name = "merge_error", columnDefinition = "TEXT")
    private String mergeError;

    /* ==========================================================
       CHILDREN â€” DEI changes on this segment
       ========================================================== */

    @OneToMany(
            mappedBy = "segmentChange",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("deiCode ASC, sequenceOrder ASC")
    private List<ScheduleDeiChangeEntity> deiChanges = new ArrayList<>();

    /* ==========================================================
       HELPERS
       ========================================================== */

    public void addDeiChange(ScheduleDeiChangeEntity deiChange) {
        if (deiChange != null) {
            deiChanges.add(deiChange);
            deiChange.setSegmentChange(this);
        }
    }
}
