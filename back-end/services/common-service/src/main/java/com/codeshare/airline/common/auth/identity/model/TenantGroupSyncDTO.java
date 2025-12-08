package com.codeshare.airline.common.auth.identity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantGroupSyncDTO {

    private UUID tenantGroupId;     // ID from TenantOrganizationGroup
    private UUID tenantId;          // Tenant ID
    private UUID organizationId;    // Organization ID

    private String name;
    private String code;
    private String description;
}
