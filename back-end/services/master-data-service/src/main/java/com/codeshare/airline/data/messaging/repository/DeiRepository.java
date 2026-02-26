package com.codeshare.airline.data.messaging.repository;

import com.codeshare.airline.data.messaging.eitities.DeiRegistry;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeiRepository
        extends CSMDataBaseRepository<DeiRegistry, UUID> {

    Optional<DeiRegistry> findByDeiNumber(String deiNumber);
}