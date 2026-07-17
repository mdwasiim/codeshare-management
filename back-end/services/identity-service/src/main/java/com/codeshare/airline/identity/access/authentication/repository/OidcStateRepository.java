package com.codeshare.airline.identity.access.authentication.repository;

import com.codeshare.airline.identity.access.authentication.entities.OidcStateEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface OidcStateRepository extends CSMDataBaseRepository<OidcStateEntity, Long> {

    Optional<OidcStateEntity> findByState(String state);
}
