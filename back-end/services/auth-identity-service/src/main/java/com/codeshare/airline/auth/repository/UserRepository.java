package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.jpa.audit.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByTenantId(UUID tenantId);

    List<User> findByOrganizationId(UUID organizationId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}