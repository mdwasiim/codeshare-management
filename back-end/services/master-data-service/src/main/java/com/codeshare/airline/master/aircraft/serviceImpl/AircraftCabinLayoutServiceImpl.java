package com.codeshare.airline.master.aircraft.serviceImpl;


import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftCabinLayout;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.repository.AircraftCabinLayoutRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.service.AircraftCabinLayoutService;
import com.codeshare.airline.master.aircraft.mappers.AircraftCabinLayoutMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftCabinLayoutServiceImpl
        extends BaseServiceImpl<AircraftCabinLayout, AircraftCabinLayoutDTO, Long>
        implements AircraftCabinLayoutService {

    private final AircraftCabinLayoutRepository repository;
    private final AircraftConfigurationRepository configRepository;

    public AircraftCabinLayoutServiceImpl(
            AircraftCabinLayoutRepository repository,
            AircraftCabinLayoutMapper mapper,
            AircraftConfigurationRepository configRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.configRepository = configRepository;
    }

    private AircraftConfiguration getConfig(Long id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuration not found"));
    }

    private void validateSeatCapacity(
            Long configId,
            Integer newSeatCount,
            Long existingLayoutId) {

        int currentTotal = repository.sumSeatCount(configId);

        if (existingLayoutId != null) {
            AircraftCabinLayout existing =
                    repository.findById(existingLayoutId)
                            .orElseThrow(() -> new EntityNotFoundException("Cabin layout not found"));

            currentTotal -= existing.getSeatCount();
        }

        if (currentTotal + newSeatCount >
                getConfig(configId).getTotalSeats()) {

            throw new IllegalStateException(
                    "Total cabin seats exceed aircraft configuration total seats."
            );
        }
    }

    @Override
    public AircraftCabinLayoutDTO create(AircraftCabinLayoutDTO dto) {

        AircraftConfiguration config =
                getConfig(dto.getAircraftConfigurationId());

        validateSeatCapacity(
                config.getId(),
                dto.getSeatCount(),
                null
        );

        AircraftCabinLayout layout = mapper.toEntity(dto);
        layout.setAircraftConfiguration(config);

        return mapper.toDTO(repository.save(layout));
    }

    @Override
    public AircraftCabinLayoutDTO update(Long id,
                                         AircraftCabinLayoutDTO dto) {

        AircraftCabinLayout existing =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Cabin layout not found"));

        validateSeatCapacity(
                existing.getAircraftConfiguration().getId(),
                dto.getSeatCount(),
                id
        );

        mapper.updateEntityFromDto(dto, existing);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<AircraftCabinLayoutDTO> getByConfiguration(Long configId) {

        // Validate configuration exists (optional but recommended)
        if (!configRepository.existsById(configId)) {
            throw new EntityNotFoundException("Configuration not found");
        }

        return mapper.toDTOList(
                repository.findByAircraftConfigurationId(configId)
        );
    }
}