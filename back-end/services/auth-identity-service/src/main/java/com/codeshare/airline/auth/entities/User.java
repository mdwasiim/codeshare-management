package com.codeshare.airline.auth.entities;

import com.codeshare.airline.core.enums.AuthSource;
import com.codeshare.airline.core.enums.UserStatus;
import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_user_name", columnNames = "user_name"),
                @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
                @UniqueConstraint(
                        name = "uk_user_external_auth",
                        columnNames = {"external_id", "auth_source"}
                )
        },
        indexes = {
                @Index(name = "idx_user_user_name", columnList = "user_name")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class User extends CSMDataAbstractEntity {

    @Column(name = "user_name", nullable = false, length = 150)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_source", nullable = false)
    private AuthSource authSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "first_name", length = 150)
    private String firstName;

    @Column(name = "last_name", length = 150)
    private String lastName;

    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "last_login_provider", length = 30)
    private String lastLoginProvider;

    @Column(name = "external_id", length = 50)
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

}
