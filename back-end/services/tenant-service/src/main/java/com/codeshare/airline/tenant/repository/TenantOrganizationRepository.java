package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.common.services.jpa.BaseRepository;
import com.codeshare.airline.tenant.entities.TenantOrganization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantOrganizationRepository extends BaseRepository<TenantOrganization, UUID> {

    Optional<TenantOrganization> findByName(String name);

    List<TenantOrganization> findByTenantId(UUID tenantId);

    List<TenantOrganization> findByParentId(UUID parentId);

    boolean existsByCodeAndTenantId(String code, UUID tenantId);

    Optional<TenantOrganization> findByCodeAndTenantCode(String orgCode, String code);
}
