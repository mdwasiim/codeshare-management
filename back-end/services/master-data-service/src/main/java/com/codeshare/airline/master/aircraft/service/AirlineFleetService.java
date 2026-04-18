package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.dto.aircraft.AirlineFleetDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface AirlineFleetService
        extends BaseService<AirlineFleetDTO, UUID> {

    List<AirlineFleetDTO> getByAirline(UUID airlineId);

    List<AirlineFleetDTO> getByConfiguration(UUID configId);
}