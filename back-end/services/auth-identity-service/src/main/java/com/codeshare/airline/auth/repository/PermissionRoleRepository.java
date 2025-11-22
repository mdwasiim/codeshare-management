package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PermissionRoleRepository extends JpaRepository<PermissionRole, UUID> {

    List<PermissionRole> findByPermissionId(UUID permissionId);

    List<PermissionRole> findByRoleId(UUID roleId);

    boolean existsByPermissionIdAndRoleId(UUID permissionId, UUID roleId);


}
