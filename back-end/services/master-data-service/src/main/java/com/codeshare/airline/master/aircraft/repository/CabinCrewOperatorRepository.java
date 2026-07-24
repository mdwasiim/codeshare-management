package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.CabinCrewEmployer;

import java.util.Optional;

public interface CabinCrewOperatorRepository
        extends CSMDataBaseRepository<CabinCrewEmployer, Long> {

    Optional<CabinCrewEmployer> findByEmployerCode(String employerCode);
}
