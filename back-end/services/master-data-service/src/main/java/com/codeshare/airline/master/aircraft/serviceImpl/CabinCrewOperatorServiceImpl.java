package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.aircraft.CabinCrewOperatorDTO;
import com.codeshare.airline.master.aircraft.entities.CabinCrewOperator;
import com.codeshare.airline.master.aircraft.mappers.CabinCrewOperatorMapper;
import com.codeshare.airline.master.aircraft.repository.CabinCrewOperatorRepository;
import com.codeshare.airline.master.aircraft.service.CabinCrewOperatorService;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.georegion.eitities.Country;
import com.codeshare.airline.master.georegion.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CabinCrewOperatorServiceImpl
        extends BaseServiceImpl<CabinCrewOperator, CabinCrewOperatorDTO, UUID>
        implements CabinCrewOperatorService {

    private final CabinCrewOperatorRepository repository;
    private final CountryRepository countryRepository;
    private final AirlineCarrierRepository airlineCarrierRepository;

    public CabinCrewOperatorServiceImpl(
            CabinCrewOperatorRepository repository,
            CabinCrewOperatorMapper mapper,
            CountryRepository countryRepository,
            AirlineCarrierRepository airlineCarrierRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.countryRepository = countryRepository;
        this.airlineCarrierRepository = airlineCarrierRepository;
    }

    @Override
    public CabinCrewOperatorDTO create(CabinCrewOperatorDTO dto) {
        CabinCrewOperator entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CabinCrewOperatorDTO update(UUID id, CabinCrewOperatorDTO dto) {
        CabinCrewOperator existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cabin crew operator not found"));

        mapper.updateEntityFromDto(dto, existing);
        setRelationships(existing, dto);
        return mapper.toDTO(repository.save(existing));
    }

    private void setRelationships(CabinCrewOperator entity, CabinCrewOperatorDTO dto) {
        entity.setCountry(getCountry(dto.getCountryId()));
        entity.setAirline(getAirlineCarrier(dto.getAirlineId()));
    }

    private Country getCountry(UUID id) {
        if (id == null) {
            return null;
        }

        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }

    private AirlineCarrier getAirlineCarrier(UUID id) {
        if (id == null) {
            return null;
        }

        return airlineCarrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline carrier not found"));
    }
}
