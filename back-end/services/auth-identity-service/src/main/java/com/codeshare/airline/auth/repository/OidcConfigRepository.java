package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.model.entities.OidcConfigEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface OidcConfigRepository  extends CSMDataBaseRepository<OidcConfigEntity, UUID> {

    boolean existsByIdentityProvider_Id(UUID identityProviderId);
}
