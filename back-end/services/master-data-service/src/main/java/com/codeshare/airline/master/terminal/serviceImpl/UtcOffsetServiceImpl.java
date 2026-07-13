package com.codeshare.airline.master.terminal.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.terminal.UtcOffsetDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import com.codeshare.airline.master.terminal.entities.UtcOffset;
import com.codeshare.airline.master.terminal.mappers.UtcOffsetMapper;
import com.codeshare.airline.master.terminal.repository.UtcOffsetRepository;
import com.codeshare.airline.master.terminal.service.UtcOffsetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UtcOffsetServiceImpl
        extends BaseServiceImpl<UtcOffset, UtcOffsetDTO, Long>
        implements UtcOffsetService {

    private final TimezoneRepository timezoneRepository;

    public UtcOffsetServiceImpl(UtcOffsetRepository repository,
                                UtcOffsetMapper mapper,
                                TimezoneRepository timezoneRepository) {
        super(repository, mapper);
        this.timezoneRepository = timezoneRepository;
    }

    private Timezone getTimezone(Long timezoneId) {
        if (timezoneId == null) {
            return null;
        }

        return timezoneRepository.findById(timezoneId)
                .orElseThrow(() -> new EntityNotFoundException("Timezone not found"));
    }

    @Override
    public UtcOffsetDTO create(UtcOffsetDTO dto) {
        UtcOffset entity = mapper.toEntity(dto);
        entity.setTimezone(getTimezone(dto.getTimezoneId()));

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public UtcOffsetDTO update(Long id, UtcOffsetDTO dto) {
        UtcOffset existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UTC offset not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setTimezone(getTimezone(dto.getTimezoneId()));

        return mapper.toDTO(repository.save(existing));
    }
}
