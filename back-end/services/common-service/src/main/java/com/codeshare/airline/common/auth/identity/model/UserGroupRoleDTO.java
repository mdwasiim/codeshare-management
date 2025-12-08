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
public class UserGroupRoleDTO extends AuditBaseDTO {
    private UUID id;
    private UUID userId;
    private UUID groupId;
    private UUID roleId;
}
