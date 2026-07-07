package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Codeshare marketing carrier designator for a live flight leg.
 *
 * Decoded from SSIM Chapter 5, Record Type 4 (T4) DEI codes:
 *   DEI 010 — Marketing carrier designator (airline code + flight number)
 *   DEI 011 — Marketing carrier booking designator (cabin class mapping)
 *
 * Stored as a first-class entity rather than buried in raw DEI text
 * because codeshare designators are a core concept in the codeshare
 * management domain and need to be queryable, diffable, and publishable
 * independently via SSM/ASM messages (Chapter 7).
 *
 * One leg may have multiple marketing carriers (e.g. EK + QF on the same
 * operating EK flight). Order is defined by {@code sequenceOrder}.
 */
@Entity
@Table(
        name = "live_codeshare_designator",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lcd_leg",          columnList = "flight_leg_id"),
                @Index(name = "idx_lcd_marketing",    columnList = "marketing_airline_code, marketing_flight_number"),
                @Index(name = "idx_lcd_leg_mktg",     columnList = "flight_leg_id, marketing_airline_code"),
                @Index(name = "idx_lcd_board_off",    columnList = "board_point, off_point")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_live_codeshare_designator",
                        columnNames = {
                                "flight_leg_id",
                                "marketing_airline_code",
                                "marketing_flight_number",
                                "board_point",
                                "off_point"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LiveCodeshareDesignatorEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_leg_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_lcd_flight_leg")
    )
    private LiveFlightLegEntity flightLeg;

    /* ==========================================================
       DEI 010 — MARKETING CARRIER DESIGNATOR
       T4 Bytes 40–46 (within 155-byte payload):
         Bytes 40–42: marketing airline code (IATA 2 or ICAO 3)
         Bytes 43–46: marketing flight number (zero-padded)
       ========================================================== */

    @Column(name = "marketing_airline_code", length = 3, nullable = false)
    private String marketingAirlineCode;

    @Column(name = "marketing_flight_number", length = 4, nullable = false)
    private String marketingFlightNumber;

    // Marketing operational suffix (if any)
    @Column(name = "marketing_operational_suffix", length = 1)
    private String marketingOperationalSuffix;

    /* ==========================================================
       SEGMENT SCOPE (board/off-point for this codeshare pairing)
       T4 Bytes 34–39 scope fields
       ========================================================== */

    @Column(name = "board_point", length = 3)
    private String boardPoint;                  // blank = full leg scope

    @Column(name = "off_point", length = 3)
    private String offPoint;                    // blank = full leg scope

    /* ==========================================================
       DEI 011 — MARKETING BOOKING DESIGNATOR
       Cabin-class mapping from operating cabin to marketing cabin
       (e.g. operating Y → marketing J for premium codeshare)
       ========================================================== */

    // Raw booking designator string from DEI 011 payload
    @Column(name = "marketing_booking_designator", length = 20)
    private String marketingBookingDesignator;

    /* ==========================================================
       CODESHARE RELATIONSHIP TYPE
       ========================================================== */

    // true = this leg is operated under a codeshare agreement
    // false = plain interline / other marketing arrangement
    @Column(name = "is_codeshare", nullable = false)
    private boolean codeshare = true;

    // Source DEI code that produced this record ("010" or "011")
    @Column(name = "source_dei_code", length = 3)
    private String sourceDeiCode;

    /* ==========================================================
       ORDERING
       ========================================================== */

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;
}
