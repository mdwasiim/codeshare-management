package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.MergeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Groups all detected changes for a single flight identity within one
 * comparison run.
 *
 * A flight identity = airlineCode + flightNumber + operationalSuffix
 *                   + itineraryVariationId
 *
 * One flight may have changes on multiple legs (e.g. leg 1 TIM change,
 * leg 2 CNL). These are stored as {@link ScheduleLegChangeEntity} children.
 *
 * DEI-level changes on any leg are stored as {@link ScheduleDeiChangeEntity}
 * children alongside the leg change.
 */
@Entity
@Table(
        name = "schedule_flight_change",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_sfc_run",         columnList = "comparison_run_id"),
                @Index(name = "idx_sfc_airline_flt",  columnList = "airline_code, flight_number"),
                @Index(name = "idx_sfc_merge_status", columnList = "merge_status")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sfc_run_flight",
                        columnNames = {
                                "comparison_run_id",
                                "airline_code",
                                "flight_number",
                                "operational_suffix",
                                "itinerary_variation_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleFlightChangeEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP TO RUN
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "comparison_run_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_sfc_run")
    )
    private ScheduleComparisonRunEntity comparisonRun;

    /* ==========================================================
       FLIGHT IDENTITY
       ========================================================== */

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Column(name = "flight_number", length = 4, nullable = false)
    private String flightNumber;

    @Column(name = "operational_suffix", length = 1)
    private String operationalSuffix;

    @Column(name = "itinerary_variation_id", length = 2)
    private String itineraryVariationId;

    /* ==========================================================
       MERGE STATUS  (flight-level rollup)
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "merge_status", length = 20, nullable = false)
    private MergeStatus mergeStatus;

    /* ==========================================================
       CHILDREN
       ========================================================== */

    @OneToMany(
            mappedBy = "flightChange",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("legSequenceNumber ASC")
    private List<ScheduleLegChangeEntity> legChanges = new ArrayList<>();

    /* ==========================================================
       HELPERS
       ========================================================== */

    public void addLegChange(ScheduleLegChangeEntity legChange) {
        if (legChange != null) {
            legChanges.add(legChange);
            legChange.setFlightChange(this);
        }
    }
}
