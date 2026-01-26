package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends CSMDataBaseRepository<Tenant, UUID> {

    boolean existsByTenantCode(String code);
    Optional<Tenant> findByTenantCode(String code);

}
