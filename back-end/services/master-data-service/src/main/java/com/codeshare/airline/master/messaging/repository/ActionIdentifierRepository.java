package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface ActionIdentifierRepository extends CSMDataBaseRepository<ActionIdentifier, Long> {

    boolean existsByActionCode(String actionCode);

    Optional<ActionIdentifier> findByActionCode(String actionCode);
}