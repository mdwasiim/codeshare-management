package com.codeshare.airline.identity.repository;

import com.codeshare.airline.identity.entities.OidcStateEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface OidcStateRepository extends CSMDataBaseRepository<OidcStateEntity, UUID> {

    Optional<OidcStateEntity> findByState(String state);
}
