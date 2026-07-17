package com.codeshare.airline.schedule.ingestion.persistence.entities.schedule;

import com.codeshare.airline.schedule.ingestion.domain.enums.DeiScope;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "schedule_dei",
        indexes = {
                @Index(name = "idx_dei_seq", columnList = "sequence_order"),
                @Index(name = "idx_sch_dei_code", columnList = "dei_code"),
                @Index(name = "idx_sch_dei_scope", columnList = "dei_scope"),
                @Index(name = "idx_sch_dei_flight", columnList = "flight_id"),
                @Index(name = "idx_sch_dei_leg", columnList = "leg_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_leg_dei_seq",
                        columnNames = {"leg_id", "sequence_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleDataElementEntity extends CSMDataAbstractEntity {

    /* ================= RELATIONSHIP ================= */


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private ScheduleFlightEntity flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leg_id", nullable = true)
    private ScheduleLegEntity leg;

    /* ================= DEI DATA ================= */

    @Column(name = "dei_code", nullable = false)
    private Integer deiCode;

    @Column(name = "dei_value", length = 500)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "dei_scope", length = 10, nullable = false)
    private DeiScope scope;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    /* ================= SEGMENT CONTEXT ================= */

    @Column(name = "board_point", length = 3)
    private String boardPoint;

    @Column(name = "off_point", length = 3)
    private String offPoint;

    public void attachToFlight(ScheduleFlightEntity parentFlight) {
        if (parentFlight == null) {
            throw new IllegalArgumentException("Flight parent cannot be null");
        }
        this.flight = parentFlight;
        this.leg = null;
    }

    public void attachToLeg(ScheduleLegEntity parentLeg) {
        if (parentLeg == null) {
            throw new IllegalArgumentException("Leg parent cannot be null");
        }
        this.leg = parentLeg;
        this.flight = null;
    }

    @PrePersist
    @PreUpdate
    private void validateOwnership() {
        if (scope == null) {
            throw new IllegalStateException("Schedule DEI scope is required");
        }

        switch (scope) {
            case FLIGHT -> {
                if (flight == null || leg != null) {
                    throw new IllegalStateException("Flight-level DEI must reference exactly one flight");
                }
                boardPoint = null;
                offPoint = null;
            }
            case LEG -> {
                if (leg == null || flight != null) {
                    throw new IllegalStateException("Leg-level DEI must reference exactly one leg");
                }
                boardPoint = null;
                offPoint = null;
            }
            case SEGMENT -> {
                if (leg == null || flight != null) {
                    throw new IllegalStateException("Segment-level DEI must reference exactly one leg");
                }
                if (isBlank(boardPoint) || isBlank(offPoint)) {
                    throw new IllegalStateException("Segment-level DEI requires board and off points");
                }
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

}
