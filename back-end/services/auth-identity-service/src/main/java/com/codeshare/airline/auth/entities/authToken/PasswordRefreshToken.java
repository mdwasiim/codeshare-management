package com.codeshare.airline.auth.entities.authToken;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rt_token", columnNames = {"token"})
        },
        indexes = {
                @Index(name = "idx_rt_user", columnList = "user_id"),
                @Index(name = "idx_rt_token_hash", columnList = "token_hash"),
                @Index(name = "idx_rt_tenant", columnList = "tenant_id"),
                @Index(name = "idx_rt_expiry", columnList = "expires_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PasswordRefreshToken extends AbstractEntity {

    // Hashed Token (NEVER store raw value)
    @Column(name = "token", nullable = false, length = 255)
    private String token;

    // The token hash used for indexing / revocation checks
    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    // Rotation → new token created after refresh
    @Column(name = "replaced_by_token_hash", length = 255)
    private String replacedByTokenHash;

    // Token expiration timestamp
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    // Device / Client Metadata
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "device_id", length = 200)
    private String deviceId;

    // Multi-tenant
    @Column(name = "tenant_id", columnDefinition = "BINARY(16)")
    private UUID tenantId;

    // Token revoked status
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    // Reference to User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
