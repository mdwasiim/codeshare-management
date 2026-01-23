package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.model.entities.RolePermission;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolePermissionRepository extends CSMDataBaseRepository<RolePermission, UUID> {

    List<RolePermission> findByPermissionId(UUID permissionId);

    List<RolePermission> findByRoleId(UUID roleId);

    Optional<RolePermission> findByRoleIdAndPermissionId(UUID permissionId, UUID roleId);


}
