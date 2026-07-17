package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.messaging.entities.DeiRegistry;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface DeiRepository
        extends CSMDataBaseRepository<DeiRegistry, Long> {

    Optional<DeiRegistry> findByDeiNumber(String deiNumber);
}