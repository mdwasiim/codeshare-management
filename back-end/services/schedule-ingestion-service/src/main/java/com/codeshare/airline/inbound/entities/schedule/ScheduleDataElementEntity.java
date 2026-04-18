package com.codeshare.airline.inbound.entities.schedule;

import com.codeshare.airline.inbound.domain.enums.DeiScope;
import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
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
}