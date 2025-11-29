package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.UserGroupRole;
import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.common.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity {


    @Column(nullable = false, unique = true, length = 150)
    private String username;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 150)
    private String firstName;

    @Column(name = "last_name", length = 150)
    private String lastName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    private LocalDateTime lastLogin;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    // Correct: store only tenantId, not Tenant entity
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    // Optional: keep track of user's organization (but store only ID)
    @Column(name = "organization_id", columnDefinition = "BINARY(16)")
    private UUID organizationId;

    // Optional: primary business user group (department)
    @Column(name = "user_group_id", columnDefinition = "BINARY(16)")
    private UUID userGroupId;

    // Identity relationships (allowed)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGroupRole> userGroupRoles = new HashSet<>();

}
