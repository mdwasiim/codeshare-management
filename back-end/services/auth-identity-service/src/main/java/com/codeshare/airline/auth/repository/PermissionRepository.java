package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.rbac.Permission;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionRepository extends BaseRepository<Permission, UUID> {
    Optional<Permission> findByName(String view);
    List<Permission> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    List<Permission> findByRoleIds(Set<UUID> roleIds);

    boolean existsByTenantIdAndCode(UUID tenantId, String code);
}
