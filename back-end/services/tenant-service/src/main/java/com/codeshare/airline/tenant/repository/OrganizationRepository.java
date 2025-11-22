package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.tenant.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findByName(String name);

    List<Organization> findByTenantId(UUID tenantId);

    List<Organization> findByParentId(UUID parentId);

    boolean existsByCodeAndTenantId(String code, UUID tenantId);
}
