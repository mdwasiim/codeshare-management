package com.codeshare.airline.auth.service;

import com.codeshare.airline.common.auth.identity.model.PermissionRoleDTO;

import java.util.List;
import java.util.UUID;

public interface PermissionRoleAssignmentService {

    PermissionRoleDTO assignPermissionToRole(UUID roleId, UUID permissionId);

    void removePermissionFromRole(UUID roleId, UUID permissionId);

    List<PermissionRoleDTO> getPermissionsByRole(UUID roleId);

    List<PermissionRoleDTO> getRolesByPermission(UUID permissionId);

    List<PermissionRoleDTO> assignPermissionsToRole(UUID roleId, List<UUID> permissionIds);
}
