package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.Alliance;

import java.util.Optional;

public interface AllianceRepository extends CSMDataBaseRepository<Alliance, Long> {
    boolean existsByAllianceCode(String allianceCode);

    Optional<Alliance> findByAllianceCode(String allianceCode);
}
