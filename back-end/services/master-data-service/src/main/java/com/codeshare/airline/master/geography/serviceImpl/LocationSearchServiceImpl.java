package com.codeshare.airline.master.geography.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.georegion.LocationSearchDTO;
import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.master.geography.entities.City;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.geography.repository.CityRepository;
import com.codeshare.airline.master.geography.service.LocationSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationSearchServiceImpl implements LocationSearchService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public List<LocationSearchDTO> search(String keyword) {

        String searchKey = keyword.trim().toLowerCase();

        List<LocationSearchDTO> results = new ArrayList<>();

        // Search Airports
        List<Airport> airports =
                airportRepository.searchForLocation(searchKey);

        for (Airport airport : airports) {
            results.add(
                    LocationSearchDTO.builder()
                            .id(airport.getId())
                            .code(airport.getIataCode())
                            .name(airport.getAirportName())
                            .type("AIRPORT")
                            .countryName(
                                    airport.getCountry().getCountryName()
                            )
                            .build()
            );
        }

        // Search Cities
        List<City> cities =
                cityRepository.searchForLocation(searchKey);

        for (City city : cities) {
            results.add(
                    LocationSearchDTO.builder()
                            .id(city.getId())
                            .code(city.getIataCityCode())
                            .name(city.getCityName())
                            .type("CITY")
                            .countryName(
                                    city.getCountry().getCountryName()
                            )
                            .build()
            );
        }

        return results;
    }
}