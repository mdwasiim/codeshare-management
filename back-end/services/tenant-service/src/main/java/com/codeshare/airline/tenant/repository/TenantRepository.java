package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.common.jpa.BaseRepository;
import com.codeshare.airline.tenant.entities.Tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends BaseRepository<Tenant, UUID> {

    boolean existsByCode(String code);
    Optional<Tenant> findByCode(String code);

}
