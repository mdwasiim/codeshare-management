package com.codeshare.airline.tenant.entities;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "tenant_groups",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_group_code_tenant", columnNames = {"tenant_id", "code"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TenantOrganizationGroup extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    /* -------------------------------
       Tenant (Required)
    ------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /* -------------------------------
       TenantOrganization (Optional)
    ------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private TenantOrganization tenantOrganization;

}
