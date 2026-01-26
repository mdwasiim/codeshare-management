package com.codeshare.airline.auth.entities;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "oidc_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OidcStateEntity extends CSMDataAbstractEntity {

    /**
     * OAuth2 / OIDC state value (CSRF protection)
     */
    @Column(name = "state", length = 64, nullable = false, unique = true)
    private String state;

    /**
     * Tenant this login belongs to
     */
    @Column(name = "tenant_code", nullable = false, length = 50)
    private String tenantCode;

    // 🔐 PKCE
    @Column(name = "code_challenge", nullable = false, length = 256)
    private String codeChallenge;

    @Column(name = "code_challenge_method", nullable = false, length = 10)
    private String codeChallengeMethod; // "S256"

    /**
     * Hard expiry (usually 3–5 minutes)
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
