package com.codeshare.airline.data.aircraft.service;

import com.codeshare.airline.core.dto.aircraft.AircraftTypeDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.UUID;

public interface AircraftTypeService
        extends BaseService<AircraftTypeDTO, UUID> {

    AircraftTypeDTO getByIcao(String icao);

    AircraftTypeDTO getByModel(String model);
}