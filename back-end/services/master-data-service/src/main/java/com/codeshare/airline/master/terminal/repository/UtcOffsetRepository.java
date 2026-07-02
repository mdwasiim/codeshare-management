package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.UtcOffset;

import java.util.Optional;
import java.util.UUID;

public interface UtcOffsetRepository extends CSMDataBaseRepository<UtcOffset, UUID> {

    Optional<UtcOffset> findByOffsetCode(String offsetCode);

    boolean existsByOffsetCode(String offsetCode);
}
