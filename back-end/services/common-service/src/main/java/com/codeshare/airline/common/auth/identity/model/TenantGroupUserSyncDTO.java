package com.codeshare.airline.common.auth.identity.model;


import com.codeshare.airline.common.services.audit.AuditBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantGroupUserSyncDTO  extends AuditBaseDTO {

    // ID of the TenantOrganizationGroup (from tenant-service)
    private UUID tenantGroupId;

    // ID of Auth User (auth-identity-service user.id)
    private UUID userId;
}
