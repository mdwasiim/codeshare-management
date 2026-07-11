package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.CockpitCrewOperator;

import java.util.Optional;
import java.util.UUID;

public interface CockpitCrewOperatorRepository
        extends CSMDataBaseRepository<CockpitCrewOperator, UUID> {

    Optional<CockpitCrewOperator> findByEmployerCode(String employerCode);
}
