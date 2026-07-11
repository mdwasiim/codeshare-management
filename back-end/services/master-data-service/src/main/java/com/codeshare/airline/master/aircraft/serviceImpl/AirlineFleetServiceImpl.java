package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.aircraft.AirlineFleetDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AirlineFleetProfile;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AirlineFleetRepository;
import com.codeshare.airline.master.aircraft.service.AirlineFleetService;
import com.codeshare.airline.master.aircraft.mappers.AirlineFleetMapper;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirlineFleetServiceImpl
        extends BaseServiceImpl<AirlineFleetProfile, AirlineFleetDTO, UUID>
        implements AirlineFleetService {

    private final AirlineFleetRepository repository;
    private final AirlineCarrierRepository airlineCarrierRepository;
    private final AircraftConfigurationRepository configRepository;

    public AirlineFleetServiceImpl(
            AirlineFleetRepository repository,
            AirlineFleetMapper mapper,
            AirlineCarrierRepository airlineCarrierRepository,
            AircraftConfigurationRepository configRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.airlineCarrierRepository = airlineCarrierRepository;
        this.configRepository = configRepository;
    }

    private AirlineCarrier getAirline(UUID id) {
        return airlineCarrierRepository.findById(id)
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

        AirlineFleetProfile fleet = mapper.toEntity(dto);
        fleet.setAirline(airline);
        fleet.setAircraftConfiguration(config);

        return mapper.toDTO(repository.save(fleet));
    }

    @Override
    public AirlineFleetDTO update(UUID id, AirlineFleetDTO dto) {

        AirlineFleetProfile existing = repository.findById(id)
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