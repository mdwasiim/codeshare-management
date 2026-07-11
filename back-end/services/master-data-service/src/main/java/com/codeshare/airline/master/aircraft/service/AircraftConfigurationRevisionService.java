package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftConfigurationRevisionDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface AircraftConfigurationRevisionService
        extends BaseService<AircraftConfigurationRevisionDTO, UUID> {

    List<AircraftConfigurationRevisionDTO> getByAircraftConfiguration(UUID aircraftConfigurationId);
}
