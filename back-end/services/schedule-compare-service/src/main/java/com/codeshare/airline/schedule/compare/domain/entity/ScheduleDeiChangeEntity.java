package com.codeshare.airline.schedule.compare.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.DeiScope;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.compare.domain.enums.DeiChangeType;
import com.codeshare.airline.schedule.compare.domain.enums.ChangeSetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * A DEI-level change detected during comparison.
 *
 * Exactly one of {legChange, segmentChange} must be non-null.
 * The explicit scope field keeps that rule queryable and makes repeated
 * DEIs safe by carrying their sequence order.
 */
@Entity
@Table(
        name = "schedule_dei_change",
        schema = "schedule_compare",
        indexes = {
                @Index(name = "idx_sdc_leg_change", columnList = "leg_change_id"),
                @Index(name = "idx_sdc_segment_change", columnList = "segment_change_id"),
                @Index(name = "idx_sdc_dei_code", columnList = "dei_code"),
                @Index(name = "idx_sdc_change_type", columnList = "dei_change_type"),
                @Index(name = "idx_sdc_change_set_status", columnList = "change_set_status"),
                @Index(name = "idx_sdc_scope", columnList = "dei_scope")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sdc_leg_dei",
                        columnNames = {"leg_change_id", "dei_code", "sequence_order"}
                ),
                @UniqueConstraint(
                        name = "uk_sdc_segment_dei",
                        columnNames = {"segment_change_id", "dei_code", "sequence_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleDeiChangeEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "leg_change_id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_sdc_leg_change")
    )
    private ScheduleLegChangeEntity legChange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "segment_change_id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_sdc_segment_change")
    )
    private ScheduleSegmentChangeEntity segmentChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "dei_scope", length = 10, nullable = false)
    private DeiScope deiScope;

    @Column(name = "dei_code", length = 3, nullable = false)
    private String deiCode;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "dei_change_type", length = 10, nullable = false)
    private DeiChangeType deiChangeType;

    @Column(name = "live_value", columnDefinition = "TEXT")
    private String liveValue;

    @Column(name = "ingested_value", columnDefinition = "TEXT")
    private String ingestedValue;

    @Column(name = "live_dei_id")
    private Long liveDeiId;

    @Column(name = "ingested_dei_id")
    private Long importedDataElementId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_set_status", length = 20, nullable = false)
    private ChangeSetStatus changeSetStatus;

    @Column(name = "status_recorded_at")
    private Instant statusRecordedAt;

    @Column(name = "status_reason", columnDefinition = "TEXT")
    private String statusReason;
}

