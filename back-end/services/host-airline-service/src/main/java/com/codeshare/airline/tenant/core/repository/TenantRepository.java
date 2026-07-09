package com.codeshare.airline.tenant.core.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.core.entities.Tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends CSMDataBaseRepository<Tenant, UUID> {

    boolean existsByTenantCode(String tenantCode);

    Optional<Tenant> findByTenantCode(String tenantCode);
}
