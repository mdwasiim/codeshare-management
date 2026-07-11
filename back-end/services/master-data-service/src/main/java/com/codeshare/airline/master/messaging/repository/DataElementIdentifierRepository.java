package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.DataElementIdentifier;

import java.util.Optional;
import java.util.UUID;

public interface DataElementIdentifierRepository extends CSMDataBaseRepository<DataElementIdentifier, UUID> {

    boolean existsByDeiCode(String deiCode);

    Optional<DataElementIdentifier> findByDeiCode(String deiCode);
}
