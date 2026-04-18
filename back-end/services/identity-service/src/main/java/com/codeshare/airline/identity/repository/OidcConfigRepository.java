package com.codeshare.airline.identity.repository;

import com.codeshare.airline.identity.entities.OidcConfigEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface OidcConfigRepository  extends CSMDataBaseRepository<OidcConfigEntity, UUID> {

    boolean existsByIdentityProvider_Id(UUID identityProviderId);
}
