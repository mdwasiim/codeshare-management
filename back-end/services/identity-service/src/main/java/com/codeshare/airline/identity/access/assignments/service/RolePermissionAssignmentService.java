package com.codeshare.airline.identity.access.assignments.service;


import com.codeshare.airline.platform.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.platform.core.dto.tenant.RoleDTO;
import com.codeshare.airline.platform.core.dto.tenant.RolePermissionDTO;

import java.util.List;
import java.util.Set;

public interface RolePermissionAssignmentService {

    List<RolePermissionDTO> getPermissionsByRole(Long roleId);

    List<RolePermissionDTO> replaceRolePermissions(
            Long roleId,
            List<Long> permissionIds
    );

    List<RolePermissionDTO> getRolesByPermission(Long permissionId);

    Set<String> resolveRoleCodes(Long userId);

    Set<String> resolvePermissionCodes(Long userId);

    Set<RoleDTO> resolveRoles(Long userId);

    Set<PermissionDTO> resolvePermissions(Long userId);

}
