package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.messaging.eitities.ActionIdentifier;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActionIdentifierRepository extends CSMDataBaseRepository<ActionIdentifier, UUID> {

    boolean existsByActionCode(String actionCode);

    Optional<ActionIdentifier> findByActionCode(String actionCode);
}