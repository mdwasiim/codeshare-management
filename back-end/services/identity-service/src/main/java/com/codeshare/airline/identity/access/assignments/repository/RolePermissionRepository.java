package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RolePermissionRepository extends CSMDataBaseRepository<RolePermission, UUID> {

    List<RolePermission> findByPermissionId(UUID permissionId);

    List<RolePermission> findByRoleId(UUID roleId);

    Optional<RolePermission> findByRoleIdAndPermissionId(UUID permissionId, UUID roleId);

    @Query("select rp.role.code || ':' || rp.permission.code from RolePermission rp where rp.tenant = :tenant")
    Set<String> findMappings(@Param("tenant") Tenant tenant);

    long countByTenantId(UUID tenantId);

    @Modifying
    @Query("""
       delete from RolePermission rp
       where rp.role.id = :roleId
       """)
    void deleteByRoleId(@Param("roleId") UUID roleId);

}
