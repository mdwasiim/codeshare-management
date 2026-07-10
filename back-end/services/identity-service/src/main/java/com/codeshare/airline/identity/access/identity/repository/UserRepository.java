package com.codeshare.airline.identity.access.identity.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CSMDataBaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndTenant_Id(String username, UUID tenantId);

    boolean existsByEmailAndTenant_Id(String email, UUID tenantId);

    Optional<User> findByUsernameAndTenant_Id(String username, UUID id);

    Optional<User> findByUsernameAndTenant_TenantCode(String username, String tenantCode);

    Optional<User> findByEmailAndTenant_TenantCode(String email, String tenantCode);

    Optional<User> findByExternalIdAndTenant_TenantCodeAndAuthSource(String externalId, String tenantCode, AuthSource authSource);

    List<User> findAllByTenant_Id(UUID tenantId);

    boolean existsByUsernameAndTenant(String username, Tenant tenant);

    List<User> findByTenant(Tenant tenant);
}
