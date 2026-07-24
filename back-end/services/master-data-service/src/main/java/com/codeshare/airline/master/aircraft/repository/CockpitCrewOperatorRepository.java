package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.CockpitCrewEmployer;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface CockpitCrewOperatorRepository
        extends CSMDataBaseRepository<CockpitCrewEmployer, Long> {

    Optional<CockpitCrewEmployer> findByEmployerCode(String employerCode);
}
