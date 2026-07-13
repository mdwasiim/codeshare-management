package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.UtcOffset;

import java.util.Optional;

public interface UtcOffsetRepository extends CSMDataBaseRepository<UtcOffset, Long> {

    Optional<UtcOffset> findByOffsetCode(String offsetCode);

    boolean existsByOffsetCode(String offsetCode);
}
