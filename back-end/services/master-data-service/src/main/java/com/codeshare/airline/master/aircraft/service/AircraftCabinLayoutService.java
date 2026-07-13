package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;

public interface AircraftCabinLayoutService
        extends BaseService<AircraftCabinLayoutDTO, Long> {

    List<AircraftCabinLayoutDTO> getByConfiguration(Long configId);
}