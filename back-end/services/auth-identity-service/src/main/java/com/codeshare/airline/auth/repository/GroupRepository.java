package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    List<Group> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);
}
