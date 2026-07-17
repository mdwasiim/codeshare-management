package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.LegChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.ChangeSetStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * One detected change on a specific flight leg.
 *
 * The leg row stores the full before/after leg snapshots. Child tables store
 * repeatable sub-structures that need their own change-set lifecycle:
 * segment deltas, DEI deltas, and codeshare deltas.
 */
@Entity
@Table(
        name = "schedule_leg_change",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_slc_flight_change", columnList = "flight_change_id"),
                @Index(name = "idx_slc_change_type", columnList = "change_type"),
                @Index(name = "idx_slc_change_set_status", columnList = "change_set_status"),
                @Index(name = "idx_slc_live_leg", columnList = "live_leg_id"),
                @Index(name = "idx_slc_period", columnList = "period_start, period_end")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_slc_flight_change_leg_type",
                        columnNames = {
                                "flight_change_id",
                                "leg_sequence_number",
                                "period_start",
                                "period_end",
                                "days_of_operation",
                                "change_type"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleLegChangeEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_change_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_slc_flight_change")
    )
    private ScheduleFlightChangeEntity flightChange;

    @Column(name = "leg_sequence_number", nullable = false)
    private Integer legSequenceNumber;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "days_of_operation", length = 7)
    private String daysOfOperation;

    @Column(name = "departure_station", length = 3)
    private String departureStation;

    @Column(name = "arrival_station", length = 3)
    private String arrivalStation;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", length = 10, nullable = false)
    private LegChangeType changeType;

    @Column(name = "live_leg_id")
    private Long liveLegId;

    @Column(name = "ingested_flight_id")
    private Long importedLegId;

    @Column(name = "live_snapshot", columnDefinition = "TEXT")
    private String liveSnapshot;

    @Column(name = "ingested_snapshot", columnDefinition = "TEXT")
    private String ingestedSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_set_status", length = 20, nullable = false)
    private ChangeSetStatus changeSetStatus;

    @Column(name = "status_recorded_at")
    private Instant statusRecordedAt;

    @Column(name = "status_reason", columnDefinition = "TEXT")
    private String statusReason;

    @OneToMany(
            mappedBy = "legChange",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("boardPoint ASC, offPoint ASC")
    @Builder.Default
    private List<ScheduleSegmentChangeEntity> segmentChanges = new ArrayList<>();

    @OneToMany(
            mappedBy = "legChange",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("deiCode ASC, sequenceOrder ASC")
    @Builder.Default
    private List<ScheduleDeiChangeEntity> legDeiChanges = new ArrayList<>();

    @OneToMany(
            mappedBy = "legChange",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("sequenceOrder ASC, marketingAirlineCode ASC, marketingFlightNumber ASC")
    @Builder.Default
    private List<ScheduleCodeshareChangeEntity> codeshareChanges = new ArrayList<>();

    public void addSegmentChange(ScheduleSegmentChangeEntity segmentChange) {
        if (segmentChange != null) {
            segmentChanges.add(segmentChange);
            segmentChange.setLegChange(this);
        }
    }

    public void addLegDeiChange(ScheduleDeiChangeEntity deiChange) {
        if (deiChange != null) {
            legDeiChanges.add(deiChange);
            deiChange.setLegChange(this);
        }
    }

    public void addCodeshareChange(ScheduleCodeshareChangeEntity codeshareChange) {
        if (codeshareChange != null) {
            codeshareChanges.add(codeshareChange);
            codeshareChange.setLegChange(this);
        }
    }
}

