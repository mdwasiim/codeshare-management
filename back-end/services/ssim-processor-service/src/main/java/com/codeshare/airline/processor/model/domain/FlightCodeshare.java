package com.codeshare.airline.processor.entities.domain;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "flight_codeshare",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flight_codeshare_sched_marketing",
                        columnNames = {
                                "flight_schedule_id",
                                "marketing_carrier",
                                "marketing_flight_number"
                        }
                )
        },
        indexes = {
                @Index(name = "idx_fc_tenant", columnList = "tenant_id"),
                @Index(name = "idx_fc_schedule", columnList = "flight_schedule_id"),
                @Index(name = "idx_fc_marketing", columnList = "marketing_carrier, marketing_flight_number")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class FlightCodeshare extends CSMDataAbstractEntity {

    /* ---------------- Tenant boundary ---------------- */

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /* ---------------- Owning operational flight ---------------- */

    /**
     * The authoritative operational flight leg
     * (own-metal flight)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_schedule_id", nullable = false)
    @ToString.Exclude
    private FlightSchedule flightSchedule;

    /* ---------------- Marketing (codeshare) identity ---------------- */

    /**
     * Marketing airline designator
     * SSIM Chapter 1 / Appendix H
     */
    @Column(name = "marketing_carrier", nullable = false, length = 3)
    private String marketingCarrier;

    /**
     * Marketing flight number
     */
    @Column(name = "marketing_flight_number", nullable = false, length = 6)
    private String marketingFlightNumber;

    /* ---------------- Display & priority ---------------- */

    /**
     * Display priority among multiple marketing flights
     * Lower value = higher priority
     */
    @Column(name = "display_priority", nullable = false)
    private Integer displayPriority;

    /**
     * Whether this codeshare is visible to consumers
     * (commercial / regulatory control)
     */
    @Builder.Default
    @Column(name = "visible", nullable = false)
    private boolean visible = true;
}

