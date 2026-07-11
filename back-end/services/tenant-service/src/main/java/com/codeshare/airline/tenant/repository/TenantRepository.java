package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.Tenant;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends CSMDataBaseRepository<Tenant, UUID> {

    boolean existsByTenantCode(String tenantCode);

    @EntityGraph(attributePaths = "identityProviders")
    Optional<Tenant> findByTenantCode(String tenantCode);
}
