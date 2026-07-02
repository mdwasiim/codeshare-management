package com.codeshare.airline.master.airline.serviceImpl;

import com.codeshare.airline.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import com.codeshare.airline.master.airline.entities.Alliance;
import com.codeshare.airline.master.airline.mappers.AirlineCarrierMapper;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airline.repository.AllianceRepository;
import com.codeshare.airline.master.airline.service.AirlineCarrierService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.georegion.entities.Airport;
import com.codeshare.airline.master.georegion.entities.City;
import com.codeshare.airline.master.georegion.entities.Country;
import com.codeshare.airline.master.georegion.repository.AirportRepository;
import com.codeshare.airline.master.georegion.repository.CityRepository;
import com.codeshare.airline.master.georegion.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AirlineCarrierServiceImpl extends BaseServiceImpl<AirlineCarrier, AirlineCarrierDTO, UUID> implements AirlineCarrierService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final AllianceRepository allianceRepository;

    public AirlineCarrierServiceImpl(AirlineCarrierRepository repository, AirlineCarrierMapper mapper,
                                     CountryRepository countryRepository, CityRepository cityRepository,
                                     AirportRepository airportRepository, AllianceRepository allianceRepository) {
        super(repository, mapper);
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
        this.allianceRepository = allianceRepository;
    }

    private void applyRelations(AirlineCarrierDTO dto, AirlineCarrier entity) {
        entity.setCountry(find(countryRepository, dto.getCountryId(), "Country"));
        entity.setHeadquartersCity(find(cityRepository, dto.getHeadquartersCityId(), "Headquarters city"));
        entity.setHomeAirport(find(airportRepository, dto.getHomeAirportId(), "Home airport"));
        entity.setAlliance(find(allianceRepository, dto.getAllianceId(), "Alliance"));
    }

    private <T> T find(org.springframework.data.repository.CrudRepository<T, UUID> repo, UUID id, String name) {
        return id == null ? null : repo.findById(id).orElseThrow(() -> new EntityNotFoundException(name + " not found"));
    }

    @Override
    public AirlineCarrierDTO create(AirlineCarrierDTO dto) {
        AirlineCarrier entity = mapper.toEntity(dto);
        applyRelations(dto, entity);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AirlineCarrierDTO update(UUID id, AirlineCarrierDTO dto) {
        AirlineCarrier existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline carrier not found"));
        mapper.updateEntityFromDto(dto, existing);
        applyRelations(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }
}
