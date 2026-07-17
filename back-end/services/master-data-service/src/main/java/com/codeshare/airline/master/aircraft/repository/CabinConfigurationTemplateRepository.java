package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.CabinConfigurationTemplate;

import java.util.Optional;

public interface CabinConfigurationTemplateRepository
        extends CSMDataBaseRepository<CabinConfigurationTemplate, Long> {

    Optional<CabinConfigurationTemplate> findByConfigurationCode(String configurationCode);
}
