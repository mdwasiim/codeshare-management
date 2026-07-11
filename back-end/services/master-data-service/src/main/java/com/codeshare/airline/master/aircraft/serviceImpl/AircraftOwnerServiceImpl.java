package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftOwnerDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;
import com.codeshare.airline.master.aircraft.mappers.AircraftOwnerMapper;
import com.codeshare.airline.master.aircraft.repository.AircraftOwnerRepository;
import com.codeshare.airline.master.aircraft.service.AircraftOwnerService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AircraftOwnerServiceImpl
        extends BaseServiceImpl<AircraftOwner, AircraftOwnerDTO, UUID>
        implements AircraftOwnerService {

    private final AircraftOwnerRepository repository;
    private final CountryRepository countryRepository;

    public AircraftOwnerServiceImpl(
            AircraftOwnerRepository repository,
            AircraftOwnerMapper mapper,
            CountryRepository countryRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.countryRepository = countryRepository;
    }

    @Override
    public AircraftOwnerDTO create(AircraftOwnerDTO dto) {
        AircraftOwner entity = mapper.toEntity(dto);
        entity.setCountry(getCountry(dto.getCountryId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AircraftOwnerDTO update(UUID id, AircraftOwnerDTO dto) {
        AircraftOwner existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft owner not found"));

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
