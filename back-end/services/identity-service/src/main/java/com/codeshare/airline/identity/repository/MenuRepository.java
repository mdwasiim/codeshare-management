package com.codeshare.airline.repository;

import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.entities.Tenant;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface MenuRepository extends CSMDataBaseRepository<Menu, UUID> {

    List<Menu> findByTenant_TenantCode(String tenantCode);

    List<Menu> findByTenantIdAndParentMenuIsNull(UUID tenantId);

    List<Menu> findByTenant(Tenant tenant);

    boolean existsByTenant(Tenant tenant);

}