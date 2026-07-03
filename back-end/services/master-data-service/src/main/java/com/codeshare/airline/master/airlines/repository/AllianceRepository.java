package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.Alliance;

import java.util.Optional;
import java.util.UUID;

public interface AllianceRepository extends CSMDataBaseRepository<Alliance, UUID> {
    Optional<Alliance> findByAllianceCode(String allianceCode);
}
