package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.Role;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends CSMDataBaseRepository<Role, UUID> {

    List<Role> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    boolean existsByTenantAndCode(Tenant tenant, String code);

    List<Role> findByTenant(Tenant tenant);
}
