package com.codeshare.airline.data.aircraft.service;

import com.codeshare.airline.core.dto.aircraft.AircraftConfigurationDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.UUID;

public interface AircraftConfigurationService
        extends BaseService<AircraftConfigurationDTO, UUID> {
}