package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleSource;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Canonical live flight leg â€” source-agnostic, populated from SSIM, SSM, or ASM.
 *
 * One row per leg sequence number within a flight, covering a specific
 * period of operation. This entity holds only business-meaningful fields
 * that are common across all three message formats. Raw wire-format data
 * (byte positions, record types, serial numbers) lives exclusively in
 * schedule-ingestion-service.
 *
 * Flight identity fields (airlineCode, flightNumber, operationalSuffix,
 * itineraryVariationId) live on the parent {@link LiveFlightEntity}.
 *
 * Children:
 *  - {@link LiveSegmentEntity}              â€” board/off-point segments
 *  - {@link LiveCodeshareDesignatorEntity}  â€” marketing carrier designators
 *  - {@link LiveScheduleVersionEntity}      â€” immutable audit trail
 */
@Entity
@Table(
        name = "live_flight_leg",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lfl_flight",  columnList = "flight_id"),
                @Index(name = "idx_lfl_route",   columnList = "departure_station, arrival_station"),
                @Index(name = "idx_lfl_period",  columnList = "period_start, period_end"),
                @Index(name = "idx_lfl_status",  columnList = "leg_status"),
                @Index(name = "idx_lfl_source",  columnList = "source")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_live_flight_leg",
                        columnNames = {
                                "flight_id",
                                "leg_sequence_number",
                                "period_start",
                                "period_end",
                                "days_of_operation"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LiveFlightLegEntity extends CSMDataAbstractEntity {

    /* ==========================================================
       RELATIONSHIP
       ========================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_lfl_flight")
    )
    private LiveFlightEntity flight;

    /* ==========================================================
       SOURCE  â€” which message type last set this leg
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 10, nullable = false)
    private LiveScheduleSource source;          // SSIM | SSM | ASM | MANUAL

    /* ==========================================================
       LEG IDENTITY
       ========================================================== */

    @Column(name = "leg_sequence_number", nullable = false)
    private Integer legSequenceNumber;          // 1-based; multi-leg flight: 1, 2, 3â€¦

    // J=Passenger, C=Cargo, F=Freight, G=Positioning, Q=Technical
    @Column(name = "service_type", length = 1)
    private String serviceType;

    /* ==========================================================
       PERIOD OF OPERATION
       ========================================================== */

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    // "1234567": position 1=Mon â€¦ 7=Sun; blank position = no operation that day
    @Column(name = "days_of_operation", length = 7, nullable = false)
    private String daysOfOperation;

    // blank = operates every day within daysOfOperation
    @Column(name = "frequency_rate", length = 1)
    private String frequencyRate;

    /* ==========================================================
       DEPARTURE
       ========================================================== */

    @Column(name = "departure_station", length = 3, nullable = false)
    private String departureStation;

    // Scheduled time of departure â€” passenger-facing (local)
    @Column(name = "scheduled_departure_time")
    private LocalTime scheduledDepartureTime;

    // Scheduled time of departure â€” aircraft (may differ from passenger for ground ops)
    @Column(name = "aircraft_departure_time")
    private LocalTime aircraftDepartureTime;

    // UTC offset at departure airport, e.g. "+0300", "-0500"
    @Column(name = "departure_utc_offset", length = 5)
    private String departureUtcOffset;

    @Column(name = "departure_terminal", length = 2)
    private String departureTerminal;

    /* ==========================================================
       ARRIVAL
       ========================================================== */

    @Column(name = "arrival_station", length = 3, nullable = false)
    private String arrivalStation;

    // Scheduled time of arrival â€” aircraft
    @Column(name = "aircraft_arrival_time")
    private LocalTime aircraftArrivalTime;

    // Scheduled time of arrival â€” passenger-facing (local)
    @Column(name = "scheduled_arrival_time")
    private LocalTime scheduledArrivalTime;

    // UTC offset at arrival airport
    @Column(name = "arrival_utc_offset", length = 5)
    private String arrivalUtcOffset;

    @Column(name = "arrival_terminal", length = 2)
    private String arrivalTerminal;

    // Overnight indicator: +1 = arrives next day, +2 = two days later, blank = same day
    @Column(name = "date_variation", length = 2)
    private String dateVariation;

    /* ==========================================================
       AIRCRAFT & CONFIGURATION
       ========================================================== */

    // IATA aircraft type code, e.g. "32A", "77W", "320"
    @Column(name = "aircraft_type", length = 3)
    private String aircraftType;

    // Cabin layout string, e.g. "C12Y168"
    @Column(name = "aircraft_configuration", length = 20)
    private String aircraftConfiguration;

    /* ==========================================================
       BOOKING
       ========================================================== */

    // Booking class string per cabin, e.g. "YJCBM"
    @Column(name = "booking_designator", length = 20)
    private String bookingDesignator;

    @Column(name = "booking_modifier", length = 5)
    private String bookingModifier;

    // Meal service codes per cabin, e.g. "MBMB"
    @Column(name = "meal_service_note", length = 10)
    private String mealServiceNote;

    /* ==========================================================
       OPERATIONAL
       ========================================================== */

    // Up to 3 joint-operating airline codes (wet-lease / pool partners)
    @Column(name = "joint_operation_airlines", length = 9)
    private String jointOperationAirlines;

    // MCT (Minimum Connecting Time) status indicator
    @Column(name = "minimum_connecting_time_status", length = 2)
    private String minimumConnectingTimeStatus;

    // Y = TSA Secure Flight passenger data required
    @Column(name = "secure_flight_indicator", length = 1)
    private String secureFlightIndicator;

    // IATA code of the aircraft owner (wet-lease scenarios)
    @Column(name = "aircraft_owner", length = 3)
    private String aircraftOwner;

    @Column(name = "cockpit_crew_employer", length = 3)
    private String cockpitCrewEmployer;

    @Column(name = "cabin_crew_employer", length = 3)
    private String cabinCrewEmployer;

    // Through/onward flight for connecting service
    @Column(name = "onward_airline_designator", length = 3)
    private String onwardAirlineDesignator;

    @Column(name = "onward_flight_number", length = 4)
    private String onwardFlightNumber;

    @Column(name = "onward_operational_suffix", length = 1)
    private String onwardOperationalSuffix;

    // Y = aircraft rotates (same tail continues), N = layover
    @Column(name = "aircraft_rotation_layover", length = 1)
    private String aircraftRotationLayover;

    /* ==========================================================
       TRAFFIC RESTRICTIONS
       ========================================================== */

    // Y = intermediate transit/technical stop
    @Column(name = "flight_transit_layover", length = 1)
    private String flightTransitLayover;

    // A = operating airline identity must be disclosed to passengers
    @Column(name = "operating_airline_disclosure", length = 1)
    private String operatingAirlineDisclosure;

    // Encoded traffic restriction string, e.g. "XXXXXOOOOO"
    @Column(name = "traffic_restriction_code", length = 11)
    private String trafficRestrictionCode;

    /* ==========================================================
       LEG LIFECYCLE
       ========================================================== */

    @Enumerated(EnumType.STRING)
    @Column(name = "leg_status", length = 20, nullable = false)
    private LiveScheduleStatus legStatus;

    @Version
    @Column(name = "version")
    private Long version;

    /* ==========================================================
       CHILDREN
       ========================================================== */

    @OneToMany(
            mappedBy = "flightLeg",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("sequenceOrder ASC")
    @Builder.Default
    private List<LiveLegDataElementEntity> legDataElements = new ArrayList<>();

    @OneToMany(
            mappedBy = "flightLeg",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("boardPoint ASC, offPoint ASC")
    @Builder.Default
    private List<LiveSegmentEntity> segments = new ArrayList<>();

    @OneToMany(
            mappedBy = "flightLeg",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("sequenceOrder ASC")
    @Builder.Default
    private List<LiveCodeshareDesignatorEntity> codeshareDesignators = new ArrayList<>();

    @OneToMany(
            mappedBy = "flightLeg",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("versionNumber DESC")
    @Builder.Default
    private List<LiveScheduleVersionEntity> versions = new ArrayList<>();

    /* ==========================================================
       HELPERS
       ========================================================== */

    public void addSegment(LiveSegmentEntity segment) {
        if (segment != null) {
            segments.add(segment);
            segment.setFlightLeg(this);
        }
    }

    public void addLegDataElement(LiveLegDataElementEntity dataElement) {
        if (dataElement != null) {
            legDataElements.add(dataElement);
            dataElement.setFlightLeg(this);
        }
    }

    public void addCodeshareDesignator(LiveCodeshareDesignatorEntity cs) {
        if (cs != null) {
            codeshareDesignators.add(cs);
            cs.setFlightLeg(this);
        }
    }

    public void addVersion(LiveScheduleVersionEntity ver) {
        if (ver != null) {
            versions.add(ver);
            ver.setFlightLeg(this);
        }
    }
}
