package com.codeshare.airline.auth.entities.authentication;

import com.codeshare.airline.auth.entities.identity.User;
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
        name = "password_reset_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_prt_token", columnNames = {"token"})
        },
        indexes = {
                @Index(name = "idx_prt_user", columnList = "user_id"),
                @Index(name = "idx_prt_tenant", columnList = "tenant_id"),
                @Index(name = "idx_prt_expiry", columnList = "expires_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PasswordResetToken extends AbstractEntity {

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "token", nullable = false, length = 255)
    private String token; // already hash

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used", nullable = false)
    private boolean used = false;
}
