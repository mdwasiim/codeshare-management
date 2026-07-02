package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.CabinCrewOperator;

import java.util.Optional;
import java.util.UUID;

public interface CabinCrewOperatorRepository
        extends CSMDataBaseRepository<CabinCrewOperator, UUID> {

    Optional<CabinCrewOperator> findByEmployerCode(String employerCode);
}
