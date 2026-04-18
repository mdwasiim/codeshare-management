package com.codeshare.airline.identity.repository;


import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends CSMDataBaseRepository<Tenant, UUID> {

    boolean existsByTenantCode(String code);
    Optional<Tenant> findByTenantCode(String code);

}
