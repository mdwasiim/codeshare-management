package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    // Multi-tenancy — store only tenant UUID
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    // Optional: enable/disable permission without deleting
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PermissionRole> permissionRoles = new HashSet<>();
}
