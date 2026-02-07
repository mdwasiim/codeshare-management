package com.codeshare.airline.auth.model.entities;

import com.codeshare.airline.core.enums.TenantPlan;
import com.codeshare.airline.core.enums.TenantStatus;
import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "tenants",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_code", columnNames = "tenant_code")
        },
        indexes = {
                @Index(name = "idx_tenant_code", columnList = "tenant_code")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class Tenant extends CSMDataAbstractEntity {

    // -------------------------------
    // Tenant Basic Info
    // -------------------------------
    @Column(name = "name", nullable = false, length = 200)
    private String name;


    @EqualsAndHashCode.Include
    @Column(
            name = "tenant_code",
            nullable = false,
            updatable = false, // 🔐 IMMUTABLE
            length = 100
    )
    private String tenantCode;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TenantStatus status = TenantStatus.ACTIVE;

    // -------------------------------
    // Subscription / Plan Info
    // -------------------------------
    @Enumerated(EnumType.STRING)
    @Column(name = "plan", length = 50)
    private TenantPlan plan;


    @Column(name = "subscription_start")
    private LocalDateTime subscriptionStart;

    @Column(name = "subscription_end")
    private LocalDateTime subscriptionEnd;

    @Builder.Default
    @Column(name = "trial", nullable = false)
    private boolean trial = false;

    // -------------------------------
    // Contact & Branding Info
    // -------------------------------
    @Column(name = "contact_email", length = 200)
    private String contactEmail;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "region", length = 100)
    private String region;

    // -------------------------------
    // Identity Providers (CRITICAL)
    // -------------------------------
    @OneToMany(
            mappedBy = "tenant",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OidcIdentityProviderEntity> identityProviders;


}

