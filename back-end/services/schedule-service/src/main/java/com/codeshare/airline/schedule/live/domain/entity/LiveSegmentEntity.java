package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A board-point / off-point segment on a live flight leg â€” SSIM Chapter 5.
 *
 * In SSIM, a segment is the revenue-carrying portion between two airports
 * on a single leg. It is identified by the board point (where passengers
 * board) and the off point (where they disembark), both of which must be
 * stations on the parent {@link LiveFlightLegEntity}.
 *
 * Segment-level DEIs (Record Type 4, T4) are attached as
 * {@link LiveSegmentDeiEntity} children. Each DEI code provides a specific
 * piece of supplemental data for that board/off-point combination
 * (e.g. traffic restrictions, in-flight service info, connecting times).
 */
@Entity
@Table(
        name = "live_segment",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lseg_leg",      columnList = "flight_leg_id"),
                @Index(name = "idx_lseg_route",    columnList = "board_point, off_point"),
                @Index(name = "idx_lseg_leg_route", columnList = "flight_leg_id, board_point, off_point")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_live_segment",
                        columnNames = {"flight_leg_id", "board_point", "off_point"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LiveSegmentEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_leg_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_lseg_flight_leg")
    )
    private LiveFlightLegEntity flightLeg;

    /* ==========================================================
       SEGMENT SCOPE IDENTIFIERS
       ========================================================== */

    @Column(name = "board_point", length = 3, nullable = false)
    private String boardPoint;          // IATA 3-letter airport code

    @Column(name = "off_point", length = 3, nullable = false)
    private String offPoint;            // IATA 3-letter airport code

    /* ==========================================================
       CHILDREN â€” DEI CODES FOR THIS SEGMENT
       ========================================================== */

    @OneToMany(
            mappedBy = "segment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("sequenceOrder ASC")
    private List<LiveSegmentDeiEntity> deis = new ArrayList<>();

    /* ==========================================================
       HELPERS
       ========================================================== */

    public void addDei(LiveSegmentDeiEntity dei) {
        if (dei != null) {
            deis.add(dei);
            dei.setSegment(this);
        }
    }
}
