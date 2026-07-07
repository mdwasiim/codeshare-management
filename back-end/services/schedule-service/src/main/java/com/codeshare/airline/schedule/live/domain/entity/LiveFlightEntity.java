package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Root aggregate for a live flight — the canonical identity grouping
 * for all legs that share the same flight number and itinerary variation.
 *
 * A flight in IATA SSIM terms is uniquely identified by:
 *   airlineCode + flightNumber + operationalSuffix + itineraryVariationId
 *
 * One flight may have multiple legs (leg sequence 01, 02, 03…) which
 * together form the complete routing of that flight on a given operating day.
 * Each leg is self-contained with its own period, times, and stations.
 */
@Entity
@Table(
        name = "live_flight",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lf_airline_flt", columnList = "airline_code, flight_number"),
                @Index(name = "idx_lf_status",      columnList = "flight_status"),
                @Index(name = "idx_lf_identity",
                        columnList = "airline_code, flight_number, operational_suffix, itinerary_variation_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_live_flight",
                        columnNames = {
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
public class LiveFlightEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       FLIGHT IDENTITY  (T3 bytes 2–11)
       ========================================================== */

    // T3 Byte 2
    @Column(name = "operational_suffix", length = 1)
    private String operationalSuffix;           // e.g. "A", blank = none

    // T3 Bytes 3–5
    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;                 // IATA 2-char or ICAO 3-char

    // T3 Bytes 6–9  (zero-padded, e.g. "0234")
    @Column(name = "flight_number", length = 4, nullable = false)
    private String flightNumber;

    // T3 Bytes 10–11  ("00"–"99"; "00" = base itinerary)
    @Column(name = "itinerary_variation_id", length = 2, nullable = false)
    private String itineraryVariationId;

    // T3 Byte 128  (overflow flag when > 99 variations)
    @Column(name = "itinerary_variation_overflow", length = 1)
    private String itineraryVariationOverflow;

    /* ==========================================================
       LIVE FLIGHT STATUS
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status", length = 20, nullable = false)
    private LiveScheduleStatus flightStatus;

    /* ==========================================================
       CHILDREN
       ========================================================== */

    @OneToMany(
            mappedBy = "flight",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("legSequenceNumber ASC")
    private List<LiveFlightLegEntity> legs = new ArrayList<>();

    /* ==========================================================
       HELPERS
       ========================================================== */

    public void addLeg(LiveFlightLegEntity leg) {
        if (leg != null) {
            legs.add(leg);
            leg.setFlight(this);
        }
    }
}
