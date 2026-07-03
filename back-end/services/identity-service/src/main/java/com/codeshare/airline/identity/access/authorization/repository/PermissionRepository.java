package com.codeshare.airline.identity.access.authorization.repository;


import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;
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

    @Query("select p.code from Permission p where p.tenant = :tenant")
    Set<String> findCodesByTenant(@Param("tenant") Tenant tenant);

    long countByTenantId(UUID tenantId);

    @Query("""
    select concat(p.domain, ':', p.action)
    from Permission p
    where p.tenant.id = :tenantId
""")
    Set<String> findPermissionKeysByTenantId(UUID tenantId);
}
