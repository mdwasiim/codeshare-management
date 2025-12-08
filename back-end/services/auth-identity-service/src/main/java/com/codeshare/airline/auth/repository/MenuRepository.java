package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends BaseRepository<Menu, UUID> {
    Optional findByName(String dashboard);

    List<Menu> findByTenantId(UUID tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNull(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    Optional<Menu> findByCode(String dashboard);
}