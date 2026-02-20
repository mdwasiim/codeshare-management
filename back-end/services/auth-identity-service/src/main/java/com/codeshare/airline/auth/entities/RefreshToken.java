package com.codeshare.airline.auth.entities;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_token",
        indexes = {
                @Index(name = "idx_refresh_token_hash", columnList = "token_hash"),
                @Index(name = "idx_refresh_user", columnList = "tenant_id, username"),
                @Index(name = "idx_refresh_device", columnList = "device_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RefreshToken extends CSMDataAbstractEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "tenant_code", nullable = false, length = 50)
    private String tenantCode;

    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "device_id", length = 200)
    private String deviceId;

    /** SHA-256 hash of refresh token */
    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    /** jti claim from refresh JWT (anti-replay) */
    @Column(name = "jti", nullable = false, length = 36)
    private String jti;

    @Column(name = "auth_source", nullable = false)
    private AuthSource authSource;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
}
