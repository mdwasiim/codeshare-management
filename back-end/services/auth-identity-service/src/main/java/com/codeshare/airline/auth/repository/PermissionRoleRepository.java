package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.rbac.RolePermission;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRoleRepository extends BaseRepository<RolePermission, UUID> {

    List<RolePermission> findByPermissionId(UUID permissionId);

    List<RolePermission> findByRoleId(UUID roleId);

    Optional<RolePermission> findByRoleIdAndPermissionId(UUID permissionId, UUID roleId);


}
