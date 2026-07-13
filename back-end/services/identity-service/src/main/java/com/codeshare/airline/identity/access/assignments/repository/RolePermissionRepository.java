package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RolePermissionRepository extends CSMDataBaseRepository<RolePermission, Long> {

    List<RolePermission> findByPermissionId(Long permissionId);

    List<RolePermission> findByRoleId(Long roleId);

    Optional<RolePermission> findByRoleIdAndPermissionId(Long permissionId, Long roleId);

    @Query("select concat(rp.role.code, concat(':', rp.permission.code)) from RolePermission rp where rp.tenantId = :tenantId")
    Set<String> findMappingsByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);

    @Modifying
    @Query("""
       delete from RolePermission rp
       where rp.role.id = :roleId
       """)
    void deleteByRoleId(@Param("roleId") Long roleId);

}
