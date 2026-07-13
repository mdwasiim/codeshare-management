package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.DataElementIdentifier;

import java.util.Optional;

public interface DataElementIdentifierRepository extends CSMDataBaseRepository<DataElementIdentifier, Long> {

    boolean existsByDeiCode(String deiCode);

    Optional<DataElementIdentifier> findByDeiCode(String deiCode);
}
