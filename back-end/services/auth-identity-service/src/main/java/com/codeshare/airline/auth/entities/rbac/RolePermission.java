package com.codeshare.airline.auth.entities.rbac;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "permission_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permission_role",
                        columnNames = {"tenant_id", "permission_id", "role_id"}
                )
        },
        indexes = {
                @Index(name = "idx_perm_role_permission", columnList = "permission_id"),
                @Index(name = "idx_perm_role_role", columnList = "role_id"),
                @Index(name = "idx_perm_role_tenant", columnList = "tenant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RolePermission extends AbstractEntity {
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
