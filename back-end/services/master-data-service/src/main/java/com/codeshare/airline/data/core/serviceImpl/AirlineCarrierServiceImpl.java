package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.AirlineCarrierDTO;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.data.core.repository.AirlineCarrierRepository;
import com.codeshare.airline.data.core.service.AirlineCarrierService;
import com.codeshare.airline.data.core.utils.mappers.AirlineCarrierMapper;
import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.data.core.repository.CountryRepository;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirlineCarrierServiceImpl
        extends BaseServiceImpl<AirlineCarrier, AirlineCarrierDTO, UUID>
        implements AirlineCarrierService {

    private final AirlineCarrierRepository airlineRepository;
    private final CountryRepository countryRepository;

    public AirlineCarrierServiceImpl(
            AirlineCarrierRepository airlineRepository,
            AirlineCarrierMapper mapper,
            CountryRepository countryRepository) {

        super(airlineRepository, mapper);
        this.airlineRepository = airlineRepository;
        this.countryRepository = countryRepository;
    }

    private Country getCountry(UUID id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }

    @Override
    public AirlineCarrierDTO create(AirlineCarrierDTO dto) {

        AirlineCarrier airline = mapper.toEntity(dto);
        airline.setCountry(getCountry(dto.getCountryId()));

        return mapper.toDTO(airlineRepository.save(airline));
    }

    @Override
    public AirlineCarrierDTO update(UUID id, AirlineCarrierDTO dto) {

        AirlineCarrier existing = airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setCountry(getCountry(dto.getCountryId()));

        return mapper.toDTO(airlineRepository.save(existing));
    }

    @Override
    public AirlineCarrierDTO getByIata(String iata) {
        return airlineRepository.findByIataCode(iata.toUpperCase())
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    @Override
    public AirlineCarrierDTO getByIcao(String icao) {
        return airlineRepository.findByIcaoCode(icao.toUpperCase())
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    @Override
    public List<AirlineCarrierDTO> getByCountry(UUID countryId) {
        return mapper.toDTOList(
                airlineRepository.findByCountryId(countryId)
        );
    }
}