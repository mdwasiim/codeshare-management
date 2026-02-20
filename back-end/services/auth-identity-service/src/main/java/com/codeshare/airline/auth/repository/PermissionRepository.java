package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.Permission;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PermissionRepository extends CSMDataBaseRepository<Permission, UUID> {

    List<Permission> findByTenantId(UUID tenantId);

    @Query("""
        select pr.permission
        from RolePermission pr
        where pr.role.id in :roleIds
    """)
    List<Permission> findByRoleIds(@Param("roleIds")Set<UUID> roleIds);

    boolean existsByTenantIdAndCode(UUID tenantId, String code);

    boolean existsByTenantAndCode(Tenant tenant, String code);
}
