package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.core.dto.master.aircraft.AirlineFleetDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AirlineFleetProfile;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AirlineFleetRepository;
import com.codeshare.airline.master.aircraft.service.AirlineFleetService;
import com.codeshare.airline.master.aircraft.mappers.AirlineFleetMapper;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineFleetServiceImpl
        extends BaseServiceImpl<AirlineFleetProfile, AirlineFleetDTO, Long>
        implements AirlineFleetService {

    private final AirlineFleetRepository repository;
    private final AirlineRepository airlineRepository;
    private final AircraftConfigurationRepository configRepository;

    public AirlineFleetServiceImpl(
            AirlineFleetRepository repository,
            AirlineFleetMapper mapper,
            AirlineRepository airlineRepository,
            AircraftConfigurationRepository configRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.airlineRepository = airlineRepository;
        this.configRepository = configRepository;
    }

    private Airline getAirline(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    private AircraftConfiguration getConfig(Long id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuration not found"));
    }

    @Override
    public AirlineFleetDTO create(AirlineFleetDTO dto) {

        Airline airline = getAirline(dto.getAirlineId());
        AircraftConfiguration config = getConfig(dto.getAircraftConfigurationId());

        // 🔥 Critical Validation
        if (!config.getAirline().getId().equals(airline.getId())) {
            throw new IllegalStateException(
                    "Aircraft configuration does not belong to this airline."
            );
        }

        AirlineFleetProfile fleet = mapper.toEntity(dto);
        fleet.setAirline(airline);
        fleet.setAircraftConfiguration(config);

        return mapper.toDTO(repository.save(fleet));
    }

    @Override
    public AirlineFleetDTO update(Long id, AirlineFleetDTO dto) {

        AirlineFleetProfile existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fleet record not found"));

        Airline airline = getAirline(dto.getAirlineId());
        AircraftConfiguration config = getConfig(dto.getAircraftConfigurationId());

        if (!config.getAirline().getId().equals(airline.getId())) {
            throw new IllegalStateException(
                    "Aircraft configuration does not belong to this airline."
            );
        }

        mapper.updateEntityFromDto(dto, existing);
        existing.setAirline(airline);
        existing.setAircraftConfiguration(config);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<AirlineFleetDTO> getByAirline(Long airlineId) {
        return mapper.toDTOList(repository.findByAirlineId(airlineId));
    }

    @Override
    public List<AirlineFleetDTO> getByConfiguration(Long configId) {
        return mapper.toDTOList(repository.findByAircraftConfigurationId(configId));
    }
}