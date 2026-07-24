package com.codeshare.airline.master.airlines.serviceImpl;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.master.airlines.mappers.AirlineMapper;
import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.airlines.service.AirlineService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.geography.repository.CityRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AirlineServiceImpl extends BaseServiceImpl<Airline, AirlineCarrierDTO, Long> implements AirlineService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final AllianceRepository allianceRepository;

    public AirlineServiceImpl(AirlineRepository repository, AirlineMapper mapper,
                              CountryRepository countryRepository, CityRepository cityRepository,
                              AirportRepository airportRepository, AllianceRepository allianceRepository) {
        super(repository, mapper);
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
        this.allianceRepository = allianceRepository;
    }

    private void applyRelations(AirlineCarrierDTO dto, Airline entity) {
        entity.setCountry(find(countryRepository, dto.getCountryId(), "Country"));
        entity.setHeadquartersCity(find(cityRepository, dto.getHeadquartersCityId(), "Headquarters city"));
        entity.setHomeAirport(find(airportRepository, dto.getHomeAirportId(), "Home airport"));
        entity.setAlliance(find(allianceRepository, dto.getAllianceId(), "Alliance"));
    }

    private <T> T find(org.springframework.data.repository.CrudRepository<T, Long> repo, Long id, String name) {
        return id == null ? null : repo.findById(id).orElseThrow(() -> new EntityNotFoundException(name + " not found"));
    }

    @Override
    public AirlineCarrierDTO create(AirlineCarrierDTO dto) {
        Airline entity = mapper.toEntity(dto);
        applyRelations(dto, entity);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AirlineCarrierDTO update(Long id, AirlineCarrierDTO dto) {
        Airline existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline carrier not found"));
        mapper.updateEntityFromDto(dto, existing);
        applyRelations(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }
}
