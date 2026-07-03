package com.codeshare.airline.identity.access.authentication.repository;

import com.codeshare.airline.identity.access.authentication.entities.OidcConfigEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface OidcConfigRepository  extends CSMDataBaseRepository<OidcConfigEntity, UUID> {

    boolean existsByIdentityProvider_Id(UUID identityProviderId);
}
