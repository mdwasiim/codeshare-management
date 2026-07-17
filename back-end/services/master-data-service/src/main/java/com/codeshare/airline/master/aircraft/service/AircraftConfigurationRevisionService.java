package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftConfigurationRevisionDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;

public interface AircraftConfigurationRevisionService
        extends BaseService<AircraftConfigurationRevisionDTO, Long> {

    List<AircraftConfigurationRevisionDTO> getByAircraftConfiguration(Long aircraftConfigurationId);
}
