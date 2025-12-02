package com.codeshare.airline.common.auth.model;

import com.codeshare.airline.common.tenant.model.OrganizationDTO;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
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
public class UserDTO extends AuditBaseDto {

    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    private boolean enabled;
    private boolean accountNonLocked;

    private LocalDateTime lastLogin;

    private Boolean active;

    private TenantDTO tenant;
    private OrganizationDTO organization;

    private UUID userGroupId;

    private Set<RoleDTO> roles;
}
