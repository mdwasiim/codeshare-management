package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.messaging.entities.DeiRegistry;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeiRepository
        extends CSMDataBaseRepository<DeiRegistry, UUID> {

    Optional<DeiRegistry> findByDeiNumber(String deiNumber);
}