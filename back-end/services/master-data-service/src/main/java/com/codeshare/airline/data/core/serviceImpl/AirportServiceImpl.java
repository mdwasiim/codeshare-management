package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.AirportDTO;
import com.codeshare.airline.data.core.eitities.Airport;
import com.codeshare.airline.data.core.eitities.City;
import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.data.core.eitities.Timezone;
import com.codeshare.airline.data.core.repository.AirportRepository;
import com.codeshare.airline.data.core.repository.CityRepository;
import com.codeshare.airline.data.core.repository.CountryRepository;
import com.codeshare.airline.data.core.repository.TimezoneRepository;
import com.codeshare.airline.data.core.service.AirportService;
import com.codeshare.airline.data.core.utils.mappers.AirportMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirportServiceImpl
        extends BaseServiceImpl<Airport, AirportDTO, UUID>
        implements AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final TimezoneRepository timezoneRepository;

    public AirportServiceImpl(AirportRepository airportRepository,
                              AirportMapper mapper,
                              CityRepository cityRepository,
                              CountryRepository countryRepository,
                              TimezoneRepository timezoneRepository) {
        super(airportRepository, mapper);
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.timezoneRepository = timezoneRepository;
    }

    private City getCity(UUID id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
    }

    private Country getCountry(UUID id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }

    private Timezone getTimezone(UUID id) {
        return timezoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Timezone not found"));
    }

    @Override
    public AirportDTO create(AirportDTO dto) {

        City city = getCity(dto.getCityId());
        Country country = getCountry(dto.getCountryId());
        Timezone timezone = getTimezone(dto.getTimezoneId());

        // Validate consistency
        if (!city.getCountry().getId().equals(country.getId())) {
            throw new IllegalStateException(
                    "Airport country must match city's country."
            );
        }

        Airport airport = mapper.toEntity(dto);
        airport.setCity(city);
        airport.setCountry(country);
        airport.setTimezone(timezone);

        return mapper.toDTO(repository.save(airport));
    }

    @Override
    public AirportDTO update(UUID id, AirportDTO dto) {

        Airport existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport not found"));

        City city = getCity(dto.getCityId());
        Country country = getCountry(dto.getCountryId());
        Timezone timezone = getTimezone(dto.getTimezoneId());

        if (!city.getCountry().getId().equals(country.getId())) {
            throw new IllegalStateException(
                    "Airport country must match city's country."
            );
        }

        mapper.updateEntityFromDto(dto, existing);
        existing.setCity(city);
        existing.setCountry(country);
        existing.setTimezone(timezone);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public AirportDTO getByIata(String iata) {
        Airport airport = airportRepository.findByIataCode(iata.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found"));
        return mapper.toDTO(airport);
    }

    @Override
    public AirportDTO getByIcao(String icao) {
        Airport airport = airportRepository.findByIcaoCode(icao.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found"));
        return mapper.toDTO(airport);
    }

    @Override
    public List<AirportDTO> getByCountry(UUID countryId) {
        return mapper.toDTOList(airportRepository.findByCountryId(countryId));
    }

    @Override
    public List<AirportDTO> getByCity(UUID cityId) {
        return mapper.toDTOList(airportRepository.findByCityId(cityId));
    }

    @Override
    public List<AirportDTO> getHubAirports() {
        return mapper.toDTOList(airportRepository.findByHubTrue());
    }

    @Override
    public List<AirportDTO> getInternationalAirports() {
        return mapper.toDTOList(airportRepository.findByInternationalTrue());
    }

    @Override
    public Page<AirportDTO> search(String keyword, Pageable pageable) {
        return airportRepository.search(keyword, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public List<AirportDTO> findNearby( double latitude,double longitude,double radiusKm) {

        List<Airport> airports =airportRepository.findNearbyAirports(latitude,longitude,radiusKm);

        return mapper.toDTOList(airports);
    }
}
