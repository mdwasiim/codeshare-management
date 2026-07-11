package com.codeshare.airline.master.terminal.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.terminal.TrafficConferenceAreaDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Region;
import com.codeshare.airline.master.geography.repository.RegionRepository;
import com.codeshare.airline.master.terminal.entities.TrafficConferenceArea;
import com.codeshare.airline.master.terminal.mappers.TrafficConferenceAreaMapper;
import com.codeshare.airline.master.terminal.repository.TrafficConferenceAreaRepository;
import com.codeshare.airline.master.terminal.service.TrafficConferenceAreaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrafficConferenceAreaServiceImpl
        extends BaseServiceImpl<TrafficConferenceArea, TrafficConferenceAreaDTO, UUID>
        implements TrafficConferenceAreaService {

    private final RegionRepository regionRepository;

    public TrafficConferenceAreaServiceImpl(TrafficConferenceAreaRepository repository,
                                            TrafficConferenceAreaMapper mapper,
                                            RegionRepository regionRepository) {
        super(repository, mapper);
        this.regionRepository = regionRepository;
    }

    private Region getRegion(UUID regionId) {
        if (regionId == null) {
            return null;
        }

        return regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));
    }

    @Override
    public TrafficConferenceAreaDTO create(TrafficConferenceAreaDTO dto) {
        TrafficConferenceArea entity = mapper.toEntity(dto);
        entity.setRegion(getRegion(dto.getRegionId()));

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public TrafficConferenceAreaDTO update(UUID id, TrafficConferenceAreaDTO dto) {
        TrafficConferenceArea existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Traffic conference area not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setRegion(getRegion(dto.getRegionId()));

        return mapper.toDTO(repository.save(existing));
    }
}
