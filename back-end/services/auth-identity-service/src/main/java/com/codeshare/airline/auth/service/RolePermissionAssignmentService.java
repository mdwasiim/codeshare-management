package com.codeshare.airline.auth.service;


import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RolePermissionAssignmentService {

    RolePermissionDTO assignPermissionToRole(UUID roleId, UUID permissionId);

    void removePermissionFromRole(UUID roleId, UUID permissionId);

    List<RolePermissionDTO> getPermissionsByRole(UUID roleId);

    List<RolePermissionDTO> getRolesByPermission(UUID permissionId);

    List<RolePermissionDTO> assignPermissionsToRole(UUID roleId, List<UUID> permissionIds);
    
    Set<String> resolveRoleNames(UUID userId);

    Set<String> resolvePermissionsNames(UUID userId);

    Set<RoleDTO> resolveRoles(UUID userId);

    Set<PermissionDTO> resolvePermissions(UUID userId);

}
