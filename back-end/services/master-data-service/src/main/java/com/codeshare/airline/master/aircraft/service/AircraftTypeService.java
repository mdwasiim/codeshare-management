package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftTypeDTO;
import com.codeshare.airline.master.common.base.BaseService;


public interface AircraftTypeService
        extends BaseService<AircraftTypeDTO, Long> {

    AircraftTypeDTO getByIcao(String icao);

    AircraftTypeDTO getByModel(String model);
}