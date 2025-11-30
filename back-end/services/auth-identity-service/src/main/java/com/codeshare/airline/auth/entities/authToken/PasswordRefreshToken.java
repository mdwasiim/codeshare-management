package com.codeshare.airline.auth.entities.authToken;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_rt_user", columnList = "user_id"),
                @Index(name = "idx_rt_token_hash", columnList = "token_hash"),
                @Index(name = "idx_rt_tenant", columnList = "tenant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRefreshToken extends AbstractEntity {

    // Hashed refresh token
    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    // Rotation: new token hash linked after refresh
    @Column(name = "replaced_by_token_hash", length = 255)
    private String replacedByTokenHash;

    // Token expires
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiryDate;

    // Device + Client Info
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "device_id", length = 200)
    private String deviceId;

    // Multi-tenant support
    @Column(name = "tenant_id", columnDefinition = "BINARY(16)")
    private UUID tenantId;

    // Token is invalidated?
    @Column(nullable = false)
    private boolean revoked = false;

    // Reference to User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
