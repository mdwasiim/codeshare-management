package com.codeshare.airline.master.georegion.service;

import com.codeshare.airline.core.dto.airport.georegion.AirportDTO;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AirportService
        extends BaseService<AirportDTO, UUID> {

    AirportDTO getByIata(String iata);

    AirportDTO getByIcao(String icao);

    List<AirportDTO> getByCountry(UUID countryId);

    List<AirportDTO> getByCity(UUID cityId);

    List<AirportDTO> getHubAirports();

    List<AirportDTO> getInternationalAirports();

    Page<AirportDTO> search(String keyword, Pageable pageable);

    List<AirportDTO> findNearby( double latitude,double longitude,double radiusKm);
}