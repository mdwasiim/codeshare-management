package com.codeshare.airline.identity.access.identity.entities;

import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_tenant_user_name", columnNames = {"tenant_id", "user_name"}),
                @UniqueConstraint(name = "uk_user_tenant_email", columnNames = {"tenant_id", "email"}),
                @UniqueConstraint(name = "uk_user_tenant_external_auth", columnNames = {"tenant_id", "external_id", "auth_source"})
        },
        indexes = {
                @Index(name = "idx_user_user_name", columnList = "user_name"),
                @Index(name = "idx_user_tenant_user_name", columnList = "tenant_id,user_name")
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
    private RecordStatus recordStatus;

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

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserGroup> userGroups = new HashSet<>();
}
