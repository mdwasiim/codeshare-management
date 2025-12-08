package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.tenant.entities.TenantOrganizationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TenantOrganizationGroupRepository extends JpaRepository<TenantOrganizationGroup, UUID> {

    List<TenantOrganizationGroup> findByTenantId(UUID tenantId);

    List<TenantOrganizationGroup> findByOrganizationId(UUID orgId);

    boolean existsByTenantIdAndCode(UUID tenantId, String code);
}
