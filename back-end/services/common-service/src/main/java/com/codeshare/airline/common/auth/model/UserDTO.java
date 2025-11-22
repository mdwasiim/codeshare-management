package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.tenant.model.OrganizationDTO;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isEnabled;
    private boolean accountNonLocked;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private TenantDTO tenantDTO;
    private OrganizationDTO organizationDTOS;

    // Optional: Include roles as a list of UUIDs or DTOs
    // private Set<RoleDTO> roles;
}
