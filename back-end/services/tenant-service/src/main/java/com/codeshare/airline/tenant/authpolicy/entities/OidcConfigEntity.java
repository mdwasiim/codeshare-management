package com.codeshare.airline.tenant.authpolicy.entities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "oidc_config",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_oidc_identity_provider", columnNames = "identity_provider_id")
        },
        indexes = {
                @Index(name = "idx_oidc_issuer", columnList = "issuer_uri")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OidcConfigEntity extends CSMDataAbstractEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "identity_provider_id", nullable = false, unique = true)
    private OidcIdentityProviderEntity identityProvider;

    @Column(name = "issuer_uri", nullable = false, length = 300)
    private String issuerUri;

    @Column(name = "authorization_uri", nullable = false, length = 300)
    private String authorizationUri;

    @Column(name = "token_uri", nullable = false, length = 300)
    private String tokenUri;

    @Column(name = "jwk_set_uri", nullable = false, length = 300)
    private String jwkSetUri;

    @Column(name = "client_id", nullable = false, length = 150)
    private String clientId;

    @Column(name = "client_secret_ref", nullable = false, length = 200)
    private String clientSecretRef;

    @Column(name = "redirect_uri", nullable = false, length = 300)
    private String redirectUri;

    @Column(name = "grant_type", length = 50)
    private String grantType;

    @Column(name = "client_auth_method", length = 50)
    private String clientAuthMethod;

    @Column(name = "scopes", length = 300)
    private String scopes;

    @Column(name = "enforce_redirect_uri", nullable = false)
    private boolean enforceRedirectUri = true;
}
