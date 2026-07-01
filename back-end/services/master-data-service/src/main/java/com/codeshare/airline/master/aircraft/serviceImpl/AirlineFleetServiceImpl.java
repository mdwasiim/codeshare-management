package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.aircraft.AirlineFleetDTO;
import com.codeshare.airline.master.aircraft.eitities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.eitities.AirlineFleet;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AirlineFleetRepository;
import com.codeshare.airline.master.aircraft.service.AirlineFleetService;
import com.codeshare.airline.master.aircraft.mappers.AirlineFleetMapper;
import com.codeshare.airline.master.georegion.eitities.AirlineCarrier;
import com.codeshare.airline.master.georegion.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
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