package com.codeshare.airline.auth.entities;

import com.codeshare.airline.core.enums.AuthSource;
import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "auth_token_exchange",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_auth_token_exchange_code", columnNames = "exchange_code")
        },
        indexes = {
                @Index(name = "idx_auth_token_exchange_code", columnList = "exchange_code"),
                @Index(name = "idx_auth_token_exchange_expiry", columnList = "expires_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenExchangeEntity extends CSMDataAbstractEntity {

    /**
     * One-time exchange code used to hand off tokens to frontend.
     * Random UUID, short-lived, single-use.
     */
    @Column(name = "exchange_code", length = 64, nullable = false)
    private String exchangeCode;

    /**
     * Short-lived access token.
     * Stored temporarily (≤ 60s) for exchange coordination only.
     */
    @Column(name = "access_token", nullable = false, length = 4000)
    private String accessToken;

    /**
     * Short-lived refresh token.
     * Stored temporarily (≤ 60s) for exchange coordination only.
     */
    @Column(name = "refresh_token", nullable = false, length = 4000)
    private String refreshToken;

    /**
     * Authentication source used to obtain this exchange
     * (INTERNAL, OIDC, LDAP, SAML, etc.)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_source", nullable = false, length = 20)
    private AuthSource authSource;

    /**
     * Tenant context for which this exchange was created
     */
    @Column(name = "tenant_code", nullable = false, length = 50)
    private String tenantCode;

    /**
     * Absolute expiry time (typically now + 30–60 seconds)
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Indicates whether this exchange code has already been used
     */
    @Builder.Default
    @Column(name = "used", nullable = false)
    private boolean used = false;
}
