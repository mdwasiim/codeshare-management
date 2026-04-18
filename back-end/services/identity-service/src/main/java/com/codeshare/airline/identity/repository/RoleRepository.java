package com.codeshare.airline.identity.repository;


import com.codeshare.airline.identity.entities.Role;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends CSMDataBaseRepository<Role, UUID> {

    List<Role> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    boolean existsByTenantAndCode(Tenant tenant, String code);

    List<Role> findByTenant(Tenant tenant);
}
