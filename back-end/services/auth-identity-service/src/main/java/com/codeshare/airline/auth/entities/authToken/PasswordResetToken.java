package com.codeshare.airline.auth.entities.authToken;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "password_reset_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_prt_token", columnNames = {"token"})
        },
        indexes = {
                @Index(name = "idx_prt_user", columnList = "user_id"),
                @Index(name = "idx_prt_expiry", columnList = "expires_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PasswordResetToken extends AbstractEntity {

    // Store ONLY hashed token (security best practice)
    @Column(name = "token", nullable = false, length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    // Token is used/revoked?
    @Column(name = "used", nullable = false)
    private boolean used = false;
}
