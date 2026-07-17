package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.CockpitCrewOperator;

import java.util.Optional;

public interface CockpitCrewOperatorRepository
        extends CSMDataBaseRepository<CockpitCrewOperator, Long> {

    Optional<CockpitCrewOperator> findByEmployerCode(String employerCode);
}
