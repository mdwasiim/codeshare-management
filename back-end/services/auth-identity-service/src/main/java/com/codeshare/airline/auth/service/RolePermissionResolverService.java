package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.auth.model.RoleDTO;

import java.util.Set;
import java.util.UUID;

public interface RolePermissionResolverService {

    Set<String> resolveRoleNames(UUID userId, UUID tenantId);

    Set<String> resolvePermissionsNames(UUID userId, UUID tenantId);

    Set<RoleDTO> resolveRoles(UUID userId, UUID tenantId);

    Set<PermissionDTO> resolvePermissions(UUID userId, UUID tenantId);
}
