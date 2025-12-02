package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.common.jpa.audit.BaseRepository;
import com.codeshare.airline.tenant.entities.Organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends BaseRepository<Organization, UUID> {

    Optional<Organization> findByName(String name);

    List<Organization> findByTenantId(UUID tenantId);

    List<Organization> findByParentId(UUID parentId);

    boolean existsByCodeAndTenantId(String code, UUID tenantId);

    Optional<Organization> findByCodeAndTenantCode(String orgCode, String code);
}
