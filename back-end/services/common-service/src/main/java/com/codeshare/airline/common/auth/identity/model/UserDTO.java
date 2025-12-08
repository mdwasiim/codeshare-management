package com.codeshare.airline.common.auth.identity.model;

import com.codeshare.airline.common.services.audit.AuditBaseDTO;
import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AuditBaseDTO {

    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    private boolean enabled;
    private boolean accountNonLocked;

    private LocalDateTime lastLogin;

    private TenantDTO tenant;
    private TenantOrganizationDTO organization;

    private UUID userGroupId;

    private Set<RoleDTO> roles;
}
