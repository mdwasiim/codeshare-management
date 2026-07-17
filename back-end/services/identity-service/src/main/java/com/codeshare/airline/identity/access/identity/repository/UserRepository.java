package com.codeshare.airline.identity.access.identity.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.identity.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CSMDataBaseRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndTenantId(String username, Long tenantId);

    boolean existsByEmailAndTenantId(String email, Long tenantId);

    Optional<User> findByUsernameAndTenantId(String username, Long id);

    Optional<User> findByEmailAndTenantId(String email, Long tenantId);

    Optional<User> findByExternalIdAndTenantIdAndAuthSource(String externalId, Long tenantId, AuthSource authSource);

    List<User> findAllByTenantId(Long tenantId);

    List<User> findByTenantId(Long tenantId);
}
