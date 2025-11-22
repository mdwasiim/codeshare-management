package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.auth.model.RoleDTO;

import java.util.Set;
import java.util.UUID;

public interface RolePermissionResolverService {

    Set<RoleDTO> resolveRoleNames(UUID userId, UUID tenantId);

    Set<PermissionDTO> resolvePermissionsNames(UUID userId, UUID tenantId);
}
