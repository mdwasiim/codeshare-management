package com.codeshare.airline.identity.service.assignment;


import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RolePermissionAssignmentService {

    List<RolePermissionDTO> getPermissionsByRole(UUID roleId);

    List<RolePermissionDTO> replaceRolePermissions(
            UUID roleId,
            List<UUID> permissionIds
    );

    List<RolePermissionDTO> getRolesByPermission(UUID permissionId);

    Set<String> resolveRoleCodes(UUID userId);

    Set<String> resolvePermissionCodes(UUID userId);

    Set<RoleDTO> resolveRoles(UUID userId);

    Set<PermissionDTO> resolvePermissions(UUID userId);

}
