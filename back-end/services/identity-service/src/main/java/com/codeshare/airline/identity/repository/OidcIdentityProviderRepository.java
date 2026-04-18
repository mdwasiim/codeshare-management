package com.codeshare.airline.identity.repository;


import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.entities.OidcIdentityProviderEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OidcIdentityProviderRepository extends CSMDataBaseRepository<OidcIdentityProviderEntity, UUID> {

    Optional<OidcIdentityProviderEntity> findByTenant_TenantCodeAndAuthSource(String tenantId, AuthSource source);

    List<OidcIdentityProviderEntity> findAllByTenant_TenantCodeOrderByPriorityAsc(String tenantCode);

    Optional<OidcIdentityProviderEntity> findByTenant_IdAndAuthSource(UUID id, AuthSource authSource);
}
