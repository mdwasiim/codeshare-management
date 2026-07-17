package com.codeshare.airline.identity.access.authorization.repository;


import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends CSMDataBaseRepository<Permission, Long> {

    List<Permission> findByTenantId(Long tenantId);

    @Query("""
        select pr.permission
        from RolePermission pr
        where pr.role.id in :roleIds
    """)
    List<Permission> findByRoleIds(@Param("roleIds")Set<Long> roleIds);

    boolean existsByTenantIdAndCode(Long tenantId, String code);

    @Query("select p.code from Permission p where p.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);

    @Query("""
    select concat(p.domain, ':', p.action)
    from Permission p
    where p.tenantId = :tenantId
""")
    Set<String> findPermissionKeysByTenantId(Long tenantId);
}
