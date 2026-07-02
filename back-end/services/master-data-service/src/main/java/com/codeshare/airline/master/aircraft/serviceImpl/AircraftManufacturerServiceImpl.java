package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.aircraft.AircraftManufacturerDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftManufacturer;
import com.codeshare.airline.master.aircraft.mappers.AircraftManufacturerMapper;
import com.codeshare.airline.master.aircraft.repository.AircraftManufacturerRepository;
import com.codeshare.airline.master.aircraft.service.AircraftManufacturerService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.georegion.eitities.Country;
import com.codeshare.airline.master.georegion.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AircraftManufacturerServiceImpl
        extends BaseServiceImpl<AircraftManufacturer, AircraftManufacturerDTO, UUID>
        implements AircraftManufacturerService {

    private final AircraftManufacturerRepository repository;
    private final CountryRepository countryRepository;

    public AircraftManufacturerServiceImpl(
            AircraftManufacturerRepository repository,
            AircraftManufacturerMapper mapper,
            CountryRepository countryRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.countryRepository = countryRepository;
    }

    @Override
    public AircraftManufacturerDTO create(AircraftManufacturerDTO dto) {
        AircraftManufacturer entity = mapper.toEntity(dto);
        entity.setCountry(getCountry(dto.getCountryId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AircraftManufacturerDTO update(UUID id, AircraftManufacturerDTO dto) {
        AircraftManufacturer existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft manufacturer not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setCountry(getCountry(dto.getCountryId()));
        return mapper.toDTO(repository.save(existing));
    }

    private Country getCountry(UUID id) {
        if (id == null) {
            return null;
        }

        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }
}
