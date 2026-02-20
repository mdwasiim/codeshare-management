package com.codeshare.airline.data.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.aircraft.AirlineFleetDTO;
import com.codeshare.airline.data.aircraft.eitities.AircraftConfiguration;
import com.codeshare.airline.data.aircraft.eitities.AirlineFleet;
import com.codeshare.airline.data.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.data.aircraft.repository.AirlineFleetRepository;
import com.codeshare.airline.data.aircraft.service.AirlineFleetService;
import com.codeshare.airline.data.aircraft.utils.mappers.AirlineFleetMapper;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.data.core.repository.AirlineCarrierRepository;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirlineFleetServiceImpl
        extends BaseServiceImpl<AirlineFleet, AirlineFleetDTO, UUID>
        implements AirlineFleetService {

    private final AirlineFleetRepository repository;
    private final AirlineCarrierRepository airlineRepository;
    private final AircraftConfigurationRepository configRepository;

    public AirlineFleetServiceImpl(
            AirlineFleetRepository repository,
            AirlineFleetMapper mapper,
            AirlineCarrierRepository airlineRepository,
            AircraftConfigurationRepository configRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.airlineRepository = airlineRepository;
        this.configRepository = configRepository;
    }

    private AirlineCarrier getAirline(UUID id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    private AircraftConfiguration getConfig(UUID id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuration not found"));
    }

    @Override
    public AirlineFleetDTO create(AirlineFleetDTO dto) {

        AirlineCarrier airline = getAirline(dto.getAirlineId());
        AircraftConfiguration config = getConfig(dto.getAircraftConfigurationId());

        // 🔥 Critical Validation
        if (!config.getAirline().getId().equals(airline.getId())) {
            throw new IllegalStateException(
                    "Aircraft configuration does not belong to this airline."
            );
        }

        AirlineFleet fleet = mapper.toEntity(dto);
        fleet.setAirline(airline);
        fleet.setAircraftConfiguration(config);

        return mapper.toDTO(repository.save(fleet));
    }

    @Override
    public AirlineFleetDTO update(UUID id, AirlineFleetDTO dto) {

        AirlineFleet existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fleet record not found"));

        AirlineCarrier airline = getAirline(dto.getAirlineId());
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
    public List<AirlineFleetDTO> getByAirline(UUID airlineId) {
        return mapper.toDTOList(repository.findByAirlineId(airlineId));
    }

    @Override
    public List<AirlineFleetDTO> getByConfiguration(UUID configId) {
        return mapper.toDTOList(repository.findByAircraftConfigurationId(configId));
    }
}