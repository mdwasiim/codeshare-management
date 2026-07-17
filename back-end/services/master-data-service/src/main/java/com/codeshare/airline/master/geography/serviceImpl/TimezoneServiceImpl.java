package com.codeshare.airline.master.geography.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.georegion.TimezoneDTO;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import com.codeshare.airline.master.geography.service.TimezoneService;
import com.codeshare.airline.master.geography.mappers.TimezoneMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class TimezoneServiceImpl
        extends BaseServiceImpl<Timezone, TimezoneDTO, Long>
        implements TimezoneService {

    public TimezoneServiceImpl(TimezoneRepository repository,
                               TimezoneMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public TimezoneDTO create(TimezoneDTO dto) {

        Timezone timezone = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(timezone));
    }

    @Override
    public TimezoneDTO update(Long id, TimezoneDTO dto) {

        Timezone existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Timezone not found"));

        mapper.updateEntityFromDto(dto, existing);

        return mapper.toDTO(repository.save(existing));
    }
}
