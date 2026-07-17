package com.codeshare.airline.master.geography.service;

import com.codeshare.airline.platform.core.dto.master.georegion.AirportDTO;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AirportService
        extends BaseService<AirportDTO, Long> {

    AirportDTO getByIata(String iata);

    AirportDTO getByIcao(String icao);

    List<AirportDTO> getByCountry(Long countryId);

    List<AirportDTO> getByCity(Long cityId);

    List<AirportDTO> getHubAirports();

    List<AirportDTO> getInternationalAirports();

    Page<AirportDTO> search(String keyword, Pageable pageable);

    List<AirportDTO> findNearby( double latitude,double longitude,double radiusKm);
}