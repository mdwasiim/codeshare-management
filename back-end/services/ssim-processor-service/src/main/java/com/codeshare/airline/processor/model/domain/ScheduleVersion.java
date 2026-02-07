package com.codeshare.airline.processor.model.domain;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "schedule_version",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_schedule_version_tenant_airline_season_version",
                        columnNames = {
                                "tenant_id",
                                "airline_designator",
                                "season",
                                "version_no"
                        }
                )
        },
        indexes = {
                @Index(name = "idx_sched_ver_tenant", columnList = "tenant_id"),
                @Index(name = "idx_sched_ver_airline_season", columnList = "airline_designator, season"),
                @Index(name = "idx_sched_ver_active", columnList = "active")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class ScheduleVersion extends CSMDataAbstractEntity {

    /* ---------------- Tenant boundary ---------------- */

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /* ---------------- Schedule identity ---------------- */

    /**
     * Airline whose schedule this version represents
     * (marketing carrier perspective)
     */
    @Column(name = "airline_designator", nullable = false, length = 3)
    private String airlineDesignator;

    /**
     * Scheduling season (e.g. S26, W25)
     */
    @Column(name = "season", nullable = false, length = 6)
    private String season;

    /**
     * Monotonically increasing version number
     * New SSIM dataset → new version
     */
    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    /* ---------------- SSIM linkage ---------------- */

    /**
     * SSIM dataset that produced this schedule version
     * Immutable reference for audit & traceability
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ssim_load_id", nullable = false, updatable = false)
    @ToString.Exclude
    private SsimR1HeaderRecord ssimR1Header;

    /* ---------------- Activation control ---------------- */

    /**
     * Only ONE version per airline + season may be active
     */
    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = false;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

    @Column(name = "activated_by", length = 100)
    private String activatedBy;

    /* ---------------- Operational metadata ---------------- */

    /**
     * Optional note: cutover reason, ops comment, etc.
     */
    @Column(name = "remarks", length = 500)
    private String remarks;
}
