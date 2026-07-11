package com.codeshare.airline.identity.access.identity.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.identity.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends CSMDataBaseRepository<Role, UUID> {

    List<Role> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    boolean existsByTenantIdAndCode(UUID tenantId, String code);

    @Query("select r.code from Role r where r.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") UUID tenantId);

    long countByTenantId(UUID tenantId);
}
