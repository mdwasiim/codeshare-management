package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.CodeshareChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.MergeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Parsed codeshare delta for one marketing designator row.
 *
 * This is a first-class change record because live schedule storage keeps
 * codeshare separately from generic DEIs.
 */
@Entity
@Table(
        name = "schedule_codeshare_change",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_scc_leg_change", columnList = "leg_change_id"),
                @Index(name = "idx_scc_change_type", columnList = "change_type"),
                @Index(name = "idx_scc_merge_status", columnList = "merge_status"),
                @Index(name = "idx_scc_live_codeshare", columnList = "live_codeshare_id"),
                @Index(name = "idx_scc_marketing", columnList = "marketing_airline_code, marketing_flight_number")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_scc_leg_codeshare",
                        columnNames = {
                                "leg_change_id",
                                "marketing_airline_code",
                                "marketing_flight_number",
                                "board_point",
                                "off_point",
                                "sequence_order"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleCodeshareChangeEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "leg_change_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_scc_leg_change")
    )
    private ScheduleLegChangeEntity legChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", length = 10, nullable = false)
    private CodeshareChangeType changeType;

    @Column(name = "live_codeshare_id")
    private UUID liveCodeshareId;

    @Column(name = "ingested_source_id")
    private UUID ingestedSourceId;

    @Column(name = "marketing_airline_code", length = 3, nullable = false)
    private String marketingAirlineCode;

    @Column(name = "marketing_flight_number", length = 4, nullable = false)
    private String marketingFlightNumber;

    @Column(name = "marketing_operational_suffix", length = 1)
    private String marketingOperationalSuffix;

    @Column(name = "board_point", length = 3)
    private String boardPoint;

    @Column(name = "off_point", length = 3)
    private String offPoint;

    @Column(name = "marketing_booking_designator", length = 20)
    private String marketingBookingDesignator;

    @Column(name = "is_codeshare", nullable = false)
    private boolean codeshare = true;

    @Column(name = "source_dei_code", length = 3)
    private String sourceDeiCode;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "live_snapshot", columnDefinition = "TEXT")
    private String liveSnapshot;

    @Column(name = "ingested_snapshot", columnDefinition = "TEXT")
    private String ingestedSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "merge_status", length = 20, nullable = false)
    private MergeStatus mergeStatus;

    @Column(name = "merged_at")
    private Instant mergedAt;

    @Column(name = "merge_error", columnDefinition = "TEXT")
    private String mergeError;
}
