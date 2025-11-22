package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(String view);
    List<Permission> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);
}
