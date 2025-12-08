package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.rbac.UserRole;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends BaseRepository<UserRole, UUID> {

    List<UserRole> findByUserId(UUID userId);

    List<UserRole> findByRoleId(UUID roleId);

    boolean existsByUserIdAndRoleId(UUID userId, UUID roleId);

    List<UserRole> findByUserIdAndTenantId(UUID userId, UUID tenantId);

    Optional<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);



}
