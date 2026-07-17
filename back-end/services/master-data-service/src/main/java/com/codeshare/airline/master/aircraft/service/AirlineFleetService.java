package com.codeshare.airline.master.aircraft.service;

import com.codeshare.airline.platform.core.dto.master.aircraft.AirlineFleetDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;

public interface AirlineFleetService
        extends BaseService<AirlineFleetDTO, Long> {

    List<AirlineFleetDTO> getByAirline(Long airlineId);

    List<AirlineFleetDTO> getByConfiguration(Long configId);
}