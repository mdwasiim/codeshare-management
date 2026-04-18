package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.core.dto.aircraft.AircraftTypeDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.UUID;

public interface AircraftTypeService
        extends BaseService<AircraftTypeDTO, UUID> {

    AircraftTypeDTO getByIcao(String icao);

    AircraftTypeDTO getByModel(String model);
}