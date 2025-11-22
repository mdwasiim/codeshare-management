package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
    Optional findByName(String dashboard);

    List<Menu> findByTenantId(UUID tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNull(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);
}