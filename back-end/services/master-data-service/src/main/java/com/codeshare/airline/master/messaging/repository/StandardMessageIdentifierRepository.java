package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.StandardMessageIdentifier;

import java.util.Optional;

public interface StandardMessageIdentifierRepository extends CSMDataBaseRepository<StandardMessageIdentifier, Long> {

    boolean existsByMessageIdentifier(String messageIdentifier);

    Optional<StandardMessageIdentifier> findByMessageIdentifier(String messageIdentifier);
}
