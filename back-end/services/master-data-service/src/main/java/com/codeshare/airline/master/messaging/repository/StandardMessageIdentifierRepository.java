package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.StandardMessageIdentifier;

import java.util.Optional;
import java.util.UUID;

public interface StandardMessageIdentifierRepository extends CSMDataBaseRepository<StandardMessageIdentifier, UUID> {

    boolean existsByMessageIdentifier(String messageIdentifier);

    Optional<StandardMessageIdentifier> findByMessageIdentifier(String messageIdentifier);
}
