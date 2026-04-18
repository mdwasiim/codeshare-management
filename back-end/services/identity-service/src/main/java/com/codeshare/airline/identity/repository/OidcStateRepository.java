package com.codeshare.airline.repository;

import com.codeshare.airline.entities.OidcStateEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface OidcStateRepository extends CSMDataBaseRepository<OidcStateEntity, UUID> {

    Optional<OidcStateEntity> findByState(String state);
}
