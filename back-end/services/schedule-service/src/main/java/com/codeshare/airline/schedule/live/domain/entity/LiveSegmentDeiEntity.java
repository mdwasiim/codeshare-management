package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.core.enums.schedule.DeiCategory;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Data Element Identifier (DEI) scoped to a board-point/off-point segment.
 * Source-agnostic: populated from SSIM T4, SSM, or ASM DEI blocks.
 *
 * Flight identity is resolved via the parent chain:
 *   LiveSegmentDeiEntity → LiveSegmentEntity → LiveFlightLegEntity → LiveFlightEntity
 *
 * Common DEI codes:
 *   010 — Codeshare marketing designator (first-class: see LiveCodeshareDesignatorEntity)
 *   050 — Traffic restriction per segment
 *   070 — Minimum connecting time
 *   090 — In-flight service information
 *   100 — Bag allowance
 *   110 — Passenger SSR / special services
 *   120 — Meal service detail
 */
@Entity
@Table(
        name = "live_segment_dei",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lsd_segment",     columnList = "segment_id"),
                @Index(name = "idx_lsd_dei_code",    columnList = "data_element_identifier"),
                @Index(name = "idx_lsd_category",    columnList = "dei_category"),
                @Index(name = "idx_lsd_seg_code",    columnList = "segment_id, data_element_identifier")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_live_segment_dei",
                        columnNames = {"segment_id", "data_element_identifier", "sequence_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LiveSegmentDeiEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "segment_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_lsd_segment")
    )
    private LiveSegmentEntity segment;

    /* ==========================================================
       DEI IDENTIFIER
       ========================================================== */

    // 3-digit numeric code, e.g. "010", "050", "070"
    @Column(name = "data_element_identifier", length = 3, nullable = false)
    private String dataElementIdentifier;

    /* ==========================================================
       DEI CLASSIFICATION
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "dei_category", length = 20)
    private DeiCategory deiCategory;

    /* ==========================================================
       DEI DATA PAYLOAD
       ========================================================== */

    // DEI-code-specific payload (structure varies per code)
    @Column(name = "dei_data", length = 155)
    private String deiData;

    /* ==========================================================
       ORDERING (multiple DEIs of same code possible on a segment)
       ========================================================== */

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;
}
