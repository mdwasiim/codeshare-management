package com.codeshare.airline.tenant.entities;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tenant extends AbstractEntity {

    // -------------------------------
    // Tenant Basic Info
    // -------------------------------
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    // -------------------------------
    // Subscription / Plan Info
    // -------------------------------
    @Column(name = "plan", length = 50)
    private String plan;   // FREE, PRO, ENTERPRISE

    @Column(name = "subscription_start")
    private LocalDateTime subscriptionStart;

    @Column(name = "subscription_end")
    private LocalDateTime subscriptionEnd;

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
    // Associated Database Source
    // -------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id")
    private TenantDataSource tenantDataSource;

    // -------------------------------
    // Tenant Organizations
    // -------------------------------
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TenantOrganization> tenantOrganizations = new HashSet<>();

}
