package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftConfigurationRevisionDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AircraftConfigurationRevision;
import com.codeshare.airline.master.aircraft.mappers.AircraftConfigurationRevisionMapper;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRevisionRepository;
import com.codeshare.airline.master.aircraft.service.AircraftConfigurationRevisionService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AircraftConfigurationRevisionServiceImpl
        extends BaseServiceImpl<AircraftConfigurationRevision, AircraftConfigurationRevisionDTO, UUID>
        implements AircraftConfigurationRevisionService {

    private final AircraftConfigurationRevisionRepository repository;
    private final AircraftConfigurationRepository aircraftConfigurationRepository;

    public AircraftConfigurationRevisionServiceImpl(
            AircraftConfigurationRevisionRepository repository,
            AircraftConfigurationRevisionMapper mapper,
            AircraftConfigurationRepository aircraftConfigurationRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.aircraftConfigurationRepository = aircraftConfigurationRepository;
    }

    @Override
    public AircraftConfigurationRevisionDTO create(AircraftConfigurationRevisionDTO dto) {
        AircraftConfigurationRevision entity = mapper.toEntity(dto);
        entity.setAircraftConfiguration(getAircraftConfiguration(dto.getAircraftConfigurationId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AircraftConfigurationRevisionDTO update(UUID id, AircraftConfigurationRevisionDTO dto) {
        AircraftConfigurationRevision existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft configuration revision not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setAircraftConfiguration(getAircraftConfiguration(dto.getAircraftConfigurationId()));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AircraftConfigurationRevisionDTO> getByAircraftConfiguration(UUID aircraftConfigurationId) {
        return mapper.toDTOList(repository.findByAircraftConfigurationId(aircraftConfigurationId));
    }

    private AircraftConfiguration getAircraftConfiguration(UUID id) {
        if (id == null) {
            throw new EntityNotFoundException("Aircraft configuration is required");
        }

        return aircraftConfigurationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft configuration not found"));
    }
}
