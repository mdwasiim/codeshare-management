package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.aircraft.CabinCrewOperatorDTO;
import com.codeshare.airline.master.aircraft.entities.CabinCrewEmployer;
import com.codeshare.airline.master.aircraft.mappers.CabinCrewOperatorMapper;
import com.codeshare.airline.master.aircraft.repository.CabinCrewOperatorRepository;
import com.codeshare.airline.master.aircraft.service.CabinCrewOperatorService;
import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CabinCrewOperatorServiceImpl
        extends BaseServiceImpl<CabinCrewEmployer, CabinCrewOperatorDTO, Long>
        implements CabinCrewOperatorService {

    private final CabinCrewOperatorRepository repository;
    private final CountryRepository countryRepository;
    private final AirlineRepository airlineRepository;

    public CabinCrewOperatorServiceImpl(
            CabinCrewOperatorRepository repository,
            CabinCrewOperatorMapper mapper,
            CountryRepository countryRepository,
            AirlineRepository airlineRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.countryRepository = countryRepository;
        this.airlineRepository = airlineRepository;
    }

    @Override
    public CabinCrewOperatorDTO create(CabinCrewOperatorDTO dto) {
        CabinCrewEmployer entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CabinCrewOperatorDTO update(Long id, CabinCrewOperatorDTO dto) {
        CabinCrewEmployer existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cabin crew operator not found"));

        mapper.updateEntityFromDto(dto, existing);
        setRelationships(existing, dto);
        return mapper.toDTO(repository.save(existing));
    }

    private void setRelationships(CabinCrewEmployer entity, CabinCrewOperatorDTO dto) {
        entity.setCountry(getCountry(dto.getCountryId()));
        entity.setAirline(getAirlineCarrier(dto.getAirlineId()));
    }

    private Country getCountry(Long id) {
        if (id == null) {
            return null;
        }

        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }

    private Airline getAirlineCarrier(Long id) {
        if (id == null) {
            return null;
        }

        return airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline carrier not found"));
    }
}
