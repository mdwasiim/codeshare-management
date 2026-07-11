package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.CabinConfigurationTemplate;

import java.util.Optional;
import java.util.UUID;

public interface CabinConfigurationTemplateRepository
        extends CSMDataBaseRepository<CabinConfigurationTemplate, UUID> {

    Optional<CabinConfigurationTemplate> findByConfigurationCode(String configurationCode);
}
