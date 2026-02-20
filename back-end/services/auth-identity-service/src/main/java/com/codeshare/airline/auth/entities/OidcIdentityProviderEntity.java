package com.codeshare.airline.auth.entities;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "oidc_identity_provider",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_auth_source",
                        columnNames = {"tenant_id", "auth_source"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OidcIdentityProviderEntity extends CSMDataAbstractEntity {

    /* ================= Tenant ================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /* ================= Provider Type ================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_source", nullable = false, length = 50)
    private AuthSource authSource;

    /* ================= Provider State ================= */

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    /**
     * Lower value = higher priority
     */
    @Column(name = "priority", nullable = false)
    private int priority = 0;

    /* ================= OIDC Config ================= */

    @OneToOne(
            mappedBy = "identityProvider",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private OidcConfigEntity oidcConfig;

}
