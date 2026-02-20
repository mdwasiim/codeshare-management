package com.codeshare.airline.data.core.service;

import com.codeshare.airline.core.dto.georegion.AirlineCarrierDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface AirlineCarrierService
        extends BaseService<AirlineCarrierDTO, UUID> {

    AirlineCarrierDTO getByIata(String iata);

    AirlineCarrierDTO getByIcao(String icao);

    List<AirlineCarrierDTO> getByCountry(UUID countryId);
}