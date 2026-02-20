package com.codeshare.airline.data.aircraft.service;

import com.codeshare.airline.core.dto.aircraft.AirlineFleetDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface AirlineFleetService
        extends BaseService<AirlineFleetDTO, UUID> {

    List<AirlineFleetDTO> getByAirline(UUID airlineId);

    List<AirlineFleetDTO> getByConfiguration(UUID configId);
}