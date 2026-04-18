package com.codeshare.airline.repository;

import com.codeshare.airline.entities.Tenant;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends CSMDataBaseRepository<Tenant, UUID> {

    boolean existsByTenantCode(String code);
    Optional<Tenant> findByTenantCode(String code);

}
