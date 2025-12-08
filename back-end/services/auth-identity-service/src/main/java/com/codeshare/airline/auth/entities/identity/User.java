package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.rbac.UserGroupRole;
import com.codeshare.airline.auth.entities.rbac.UserRole;
import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_username", columnNames = {"username"}),
                @UniqueConstraint(name = "uk_user_email", columnNames = {"email"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends AbstractEntity {

    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "first_name", length = 150)
    private String firstName;

    @Column(name = "last_name", length = 150)
    private String lastName;

    // -------------------------------
    // Account State Flags
    // -------------------------------
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;


    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // -------------------------------
    // Multi-Tenant Identifiers
    // -------------------------------
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    // User Organization
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserOrganization> organizations = new HashSet<>();

    // -------------------------------
    // RBAC (User-Role, User-Group)
    // -------------------------------
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGroupRole> userGroupRoles = new HashSet<>();

}
