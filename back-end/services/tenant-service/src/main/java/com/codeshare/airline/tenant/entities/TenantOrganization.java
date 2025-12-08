package com.codeshare.airline.tenant.entities;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tenant_organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TenantOrganization extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    /* -------------------------------
       Tenant Reference (FK)
    ------------------------------- */

    // If every tenantOrganization belongs to a tenant, keep optional=false & nullable=false
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /* -------------------------------
       Self-reference: parent org
    ------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TenantOrganization parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TenantOrganization> children = new HashSet<>();

    /* -------------------------------
       UserGroups under this org
    ------------------------------- */
    @OneToMany(mappedBy = "tenantOrganization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TenantOrganizationGroup> tenantOrganizationGroups = new HashSet<>();

}
