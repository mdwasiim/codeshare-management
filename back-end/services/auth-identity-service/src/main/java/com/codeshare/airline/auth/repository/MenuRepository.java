package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.model.entities.Menu;
import com.codeshare.airline.auth.model.entities.Tenant;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends CSMDataBaseRepository<Menu, UUID> {

    List<Menu> findByTenantId(UUID tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNull(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    List<Menu> findByTenant(Tenant tenant);

    boolean existsByTenant(Tenant tenant);

    boolean existsByTenantAndCode(Tenant tenant, String code);

    Optional<Menu> findByTenantAndCode(Tenant tenant, String code);

}