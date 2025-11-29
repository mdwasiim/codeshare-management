package com.codeshare.airline.auth.entities.authToken;


import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordResetToken extends AbstractEntity {


    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="expiry", nullable = false)
    private LocalDateTime expiry;
}
