package com.codeshare.airline.tenant.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

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
    // Metadata
    // -------------------------------
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

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

    //--------------------------------
    /// Database Details
    /// ---------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_data_source_id")
    private TenantDataSource dbConfig;

    // -------------------------------
    // Organization Structure (Allowed)
    // -------------------------------
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Organization> organizations = new HashSet<>();


}
