package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_code_tenant", columnNames = {"tenant_id", "code"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Role extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    /* ----------------------------------------------
       Multi-Tenant Design:
       Identity service stores only tenantId (UUID)
    ------------------------------------------------ */
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    /* ----------------------------------------------
       RBAC Relationship Mappings
    ------------------------------------------------ */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuRole> menuRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PermissionRole> permissionRoles = new HashSet<>();
}
