package com.codeshare.airline.auth.entities.authentication;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.identity.UserDevice;
import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rt_refresh_hash", columnNames = {"refresh_token_hash"})
        },
        indexes = {
                @Index(name = "idx_rt_user", columnList = "user_id"),
                @Index(name = "idx_rt_refresh_rotation_hash", columnList = "refresh_rotation_hash"),
                @Index(name = "idx_rt_expiry", columnList = "expires_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RefreshToken extends AbstractEntity {

    // Hash of the raw refresh token
    @Column(name = "refresh_token_hash", nullable = false, length = 255)
    private String refreshTokenHash;

    // Hash of the next token created during rotation
    @Column(name = "refresh_rotation_hash", length = 255)
    private String refreshRotationHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_device_id")
    private UserDevice userDevice;
}
