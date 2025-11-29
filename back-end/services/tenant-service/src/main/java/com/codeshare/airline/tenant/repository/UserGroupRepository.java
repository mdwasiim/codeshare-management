package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.tenant.entities.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
    List<UserGroup> findByTenantId(UUID tenantId);

    Optional<UserGroup> findByCode(String groupCode);
}
