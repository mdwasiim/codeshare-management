package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.core.dto.master.aircraft.CockpitCrewOperatorDTO;
import com.codeshare.airline.master.aircraft.entities.CockpitCrewEmployer;
import com.codeshare.airline.master.aircraft.mappers.CockpitCrewOperatorMapper;
import com.codeshare.airline.master.aircraft.repository.CockpitCrewOperatorRepository;
import com.codeshare.airline.master.aircraft.service.CockpitCrewOperatorService;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CockpitCrewOperatorServiceImpl
        extends BaseServiceImpl<CockpitCrewEmployer, CockpitCrewOperatorDTO, Long>
        implements CockpitCrewOperatorService {

    private final CockpitCrewOperatorRepository repository;
    private final CountryRepository countryRepository;
    private final AirlineRepository airlineRepository;

    public CockpitCrewOperatorServiceImpl(
            CockpitCrewOperatorRepository repository,
            CockpitCrewOperatorMapper mapper,
            CountryRepository countryRepository,
            AirlineRepository airlineRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.countryRepository = countryRepository;
        this.airlineRepository = airlineRepository;
    }

    @Override
    public CockpitCrewOperatorDTO create(CockpitCrewOperatorDTO dto) {
        CockpitCrewEmployer entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CockpitCrewOperatorDTO update(Long id, CockpitCrewOperatorDTO dto) {
        CockpitCrewEmployer existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cockpit crew operator not found"));

        mapper.updateEntityFromDto(dto, existing);
        setRelationships(existing, dto);
        return mapper.toDTO(repository.save(existing));
    }

    private void setRelationships(CockpitCrewEmployer entity, CockpitCrewOperatorDTO dto) {
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
