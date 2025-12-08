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
@Table(
        name = "auth_groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_code_tenant",
                        columnNames = {"tenant_id", "code"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Group extends AbstractEntity {

    /* -------------------------------------------------------
       Sync ID from Tenant Service (TenantOrganizationGroup)
    -------------------------------------------------------- */
    @Column(name = "tenant_group_id", columnDefinition = "BINARY(16)", unique = true)
    private UUID tenantGroupId;

    /* -------------------------------------------------------
       Basic Attributes
    -------------------------------------------------------- */
    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    /* -------------------------------------------------------
       Multi-tenant fields (NO @ManyToOne)
    -------------------------------------------------------- */
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    /* -------------------------------------------------------
       RBAC relationships
    -------------------------------------------------------- */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGroup> userGroups = new HashSet<>();

}
