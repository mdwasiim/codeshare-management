package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface AircraftCabinLayoutService
        extends BaseService<AircraftCabinLayoutDTO, UUID> {

    List<AircraftCabinLayoutDTO> getByConfiguration(UUID configId);
}