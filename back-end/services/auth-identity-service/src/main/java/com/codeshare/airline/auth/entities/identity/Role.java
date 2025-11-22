package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.auth.entities.authorization.OrganizationRole;
import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.auth.entities.menu.MenuRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String code;

    @Column(length = 500)
    private String description;

    // FIX: Identity should store only tenantId
    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    private UUID tenantId;

    // RBAC Mappings
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuRole> menuRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PermissionRole> permissionRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganizationRole> organizationRoles = new HashSet<>();
}
