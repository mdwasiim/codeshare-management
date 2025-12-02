package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.common.jpa.audit.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface PermissionRoleRepository extends BaseRepository<PermissionRole, UUID> {

    List<PermissionRole> findByPermissionId(UUID permissionId);

    List<PermissionRole> findByRoleId(UUID roleId);

    boolean existsByPermissionIdAndRoleId(UUID permissionId, UUID roleId);


}
