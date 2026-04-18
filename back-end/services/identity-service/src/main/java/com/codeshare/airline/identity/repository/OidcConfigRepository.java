package com.codeshare.airline.repository;

import com.codeshare.airline.entities.OidcConfigEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface OidcConfigRepository  extends CSMDataBaseRepository<OidcConfigEntity, UUID> {

    boolean existsByIdentityProvider_Id(UUID identityProviderId);
}
