package com.codeshare.airline.tenant.entities.identity;

import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.tenant.entities.Tenant;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

@Entity
@Table(
        name = "oidc_identity_provider",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_auth_source", columnNames = {"tenant_id", "auth_source"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OidcIdentityProviderEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_source", nullable = false, length = 50)
    private AuthSource authSource;

    @Default
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Default
    @Column(name = "priority", nullable = false)
    private int priority = 0;

    @OneToOne(mappedBy = "identityProvider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OidcConfigEntity oidcConfig;
}
