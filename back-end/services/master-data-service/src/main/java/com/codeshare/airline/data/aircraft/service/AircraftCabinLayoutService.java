package com.codeshare.airline.data.aircraft.service;

import com.codeshare.airline.core.dto.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface AircraftCabinLayoutService
        extends BaseService<AircraftCabinLayoutDTO, UUID> {

    List<AircraftCabinLayoutDTO> getByConfiguration(UUID configId);
}