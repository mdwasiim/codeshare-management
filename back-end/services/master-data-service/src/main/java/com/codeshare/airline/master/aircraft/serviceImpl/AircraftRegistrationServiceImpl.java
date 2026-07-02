package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.master.aircraft.AircraftRegistrationDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;
import com.codeshare.airline.master.aircraft.entities.AircraftRegistration;
import com.codeshare.airline.master.aircraft.entities.AircraftType;
import com.codeshare.airline.master.aircraft.mappers.AircraftRegistrationMapper;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftOwnerRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftRegistrationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.aircraft.service.AircraftRegistrationService;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AircraftRegistrationServiceImpl
        extends BaseServiceImpl<AircraftRegistration, AircraftRegistrationDTO, UUID>
        implements AircraftRegistrationService {

    private final AircraftRegistrationRepository repository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final AircraftConfigurationRepository aircraftConfigurationRepository;
    private final AircraftOwnerRepository aircraftOwnerRepository;
    private final AirlineCarrierRepository airlineCarrierRepository;

    public AircraftRegistrationServiceImpl(
            AircraftRegistrationRepository repository,
            AircraftRegistrationMapper mapper,
            AircraftTypeRepository aircraftTypeRepository,
            AircraftConfigurationRepository aircraftConfigurationRepository,
            AircraftOwnerRepository aircraftOwnerRepository,
            AirlineCarrierRepository airlineCarrierRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.aircraftTypeRepository = aircraftTypeRepository;
        this.aircraftConfigurationRepository = aircraftConfigurationRepository;
        this.aircraftOwnerRepository = aircraftOwnerRepository;
        this.airlineCarrierRepository = airlineCarrierRepository;
    }

    @Override
    public AircraftRegistrationDTO create(AircraftRegistrationDTO dto) {
        AircraftRegistration entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AircraftRegistrationDTO update(UUID id, AircraftRegistrationDTO dto) {
        AircraftRegistration existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft registration not found"));

        mapper.updateEntityFromDto(dto, existing);
        setRelationships(existing, dto);
        return mapper.toDTO(repository.save(existing));
    }

    private void setRelationships(AircraftRegistration entity, AircraftRegistrationDTO dto) {
        entity.setAircraftType(getAircraftType(dto.getAircraftTypeId()));
        entity.setAircraftConfiguration(getAircraftConfiguration(dto.getAircraftConfigurationId()));
        entity.setAircraftOwner(getAircraftOwner(dto.getAircraftOwnerId()));
        entity.setOperatorAirline(getAirlineCarrier(dto.getOperatorAirlineId()));
    }

    private AircraftType getAircraftType(UUID id) {
        if (id == null) {
            throw new EntityNotFoundException("Aircraft type is required");
        }

        return aircraftTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft type not found"));
    }

    private AircraftConfiguration getAircraftConfiguration(UUID id) {
        if (id == null) {
            return null;
        }

        return aircraftConfigurationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft configuration not found"));
    }

    private AircraftOwner getAircraftOwner(UUID id) {
        if (id == null) {
            return null;
        }

        return aircraftOwnerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft owner not found"));
    }

    private AirlineCarrier getAirlineCarrier(UUID id) {
        if (id == null) {
            return null;
        }

        return airlineCarrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline carrier not found"));
    }
}
