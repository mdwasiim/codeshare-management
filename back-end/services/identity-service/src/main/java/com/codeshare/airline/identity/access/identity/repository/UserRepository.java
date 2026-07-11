package com.codeshare.airline.identity.access.identity.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.identity.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CSMDataBaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndTenantId(String username, UUID tenantId);

    boolean existsByEmailAndTenantId(String email, UUID tenantId);

    Optional<User> findByUsernameAndTenantId(String username, UUID id);

    Optional<User> findByEmailAndTenantId(String email, UUID tenantId);

    Optional<User> findByExternalIdAndTenantIdAndAuthSource(String externalId, UUID tenantId, AuthSource authSource);

    List<User> findAllByTenantId(UUID tenantId);

    List<User> findByTenantId(UUID tenantId);
}
