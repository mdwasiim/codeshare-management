package com.codeshare.airline.auth.entities.rbac;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Permission extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 150)
    private String code;          // e.g. "user:create"

    @Column(name = "domain", nullable = false, length = 100)
    private String domain;        // e.g. "user", "booking"

    @Column(name = "action", nullable = false, length = 100)
    private String action;        // e.g. "create", "manage"

    @Column(name = "description", length = 500)
    private String description;

    // Multi-tenancy — store only tenant UUID
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
