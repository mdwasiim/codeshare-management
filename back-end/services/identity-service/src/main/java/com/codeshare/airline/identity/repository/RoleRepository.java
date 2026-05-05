package com.codeshare.airline.identity.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.entities.Role;
import com.codeshare.airline.identity.entities.Tenant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends CSMDataBaseRepository<Role, UUID> {

    List<Role> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    boolean existsByTenantAndCode(Tenant tenant, String code);

    List<Role> findByTenant(Tenant tenant);

    @Query("select r.code from Role r where r.tenant = :tenant")
    Set<String> findCodesByTenant(@Param("tenant") Tenant tenant);

    long countByTenantId(UUID tenantId);
}
