package com.codeshare.airline.messaging.repository;

import com.codeshare.airline.messaging.eitities.ActionIdentifier;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActionIdentifierRepository extends CSMDataBaseRepository<ActionIdentifier, UUID> {

    boolean existsByActionCode(String actionCode);

    Optional<ActionIdentifier> findByActionCode(String actionCode);
}