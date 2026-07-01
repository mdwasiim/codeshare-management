package com.codeshare.airline.master.georegion.service;

import com.codeshare.airline.core.dto.airport.georegion.AirlineCarrierDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface AirlineCarrierService
        extends BaseService<AirlineCarrierDTO, UUID> {

    AirlineCarrierDTO getByIata(String iata);

    AirlineCarrierDTO getByIcao(String icao);

    List<AirlineCarrierDTO> getByCountry(UUID countryId);
}