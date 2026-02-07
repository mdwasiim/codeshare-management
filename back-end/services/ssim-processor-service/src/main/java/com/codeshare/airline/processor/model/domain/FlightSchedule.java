package com.codeshare.airline.processor.model.domain;


import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "flight_schedule",
        indexes = {
                @Index(name = "idx_flight_sched_tenant", columnList = "tenant_id"),
                @Index(name = "idx_flight_sched_version", columnList = "schedule_version_id"),
                @Index(name = "idx_flight_sched_flight", columnList = "carrier, flight_number"),
                @Index(name = "idx_flight_sched_route", columnList = "departure_station, arrival_station"),
                @Index(name = "idx_flight_sched_active", columnList = "active"),
                @Index( name = "idx_flight_sched_marketing_day", columnList = "carrier, flight_number, operation_date")

        }
)
@ToString(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class FlightSchedule extends CSMDataAbstractEntity {

    /* ---------------- Tenant boundary ---------------- */

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /* ---------------- Schedule version control ---------------- */

    /**
     * Schedule version that owns this flight leg
     * Exactly one active version per airline + season
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_version_id", nullable = false)
    @ToString.Exclude
    private ScheduleVersion scheduleVersion;

    /* ---------------- Flight identity ---------------- */

    /**
     * Marketing carrier (display carrier)
     */
    @Column(name = "carrier", nullable = false, length = 3)
    private String carrier;

    @Column(name = "flight_number", nullable = false, length = 6)
    private String flightNumber;

    /**
     * Operating carrier (may differ for codeshare)
     */
    @Column(name = "operating_carrier", length = 3)
    private String operatingCarrier;

    /* ---------------- Route ---------------- */

    @Column(name = "departure_station", nullable = false, length = 3)
    private String departureStation;

    @Column(name = "arrival_station", nullable = false, length = 3)
    private String arrivalStation;

    /* ---------------- Times (local) ---------------- */

    @Column(name = "scheduled_departure_time", nullable = false, length = 4)
    private String scheduledDepartureTime;

    @Column(name = "scheduled_arrival_time", nullable = false, length = 4)
    private String scheduledArrivalTime;

    /* ---------------- Period of operation ---------------- */

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    /**
     * Days of operation bitmap (e.g. 1111100)
     */
    @Column(name = "days_of_operation", nullable = false, length = 7)
    private String daysOfOperation;

    /* ---------------- Equipment & service ---------------- */

    @Column(name = "aircraft_type", length = 4)
    private String aircraftType;

    @Column(name = "service_type", length = 2)
    private String serviceType;

    /* ---------------- Codeshare indicator ---------------- */

    /**
     * TRUE if this row represents a marketing (codeshare) flight
     * FALSE for own-metal operational flights
     */
    @Column(name = "codeshare", nullable = false)
    private boolean codeshare;

    @Column(name = "operation_date", nullable = false)
    private LocalDate operationDate;

    @Column(name = "codeshare_priority")
    private Integer codesharePriority;

    @ManyToOne
    @JoinColumn(name = "source_leg_id")
    private SsimR3FlightLegRecord sourceLeg;


}
