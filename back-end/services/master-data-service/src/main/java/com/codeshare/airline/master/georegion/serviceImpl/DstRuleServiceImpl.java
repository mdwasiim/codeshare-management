package com.codeshare.airline.master.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.DstRuleDTO;
import com.codeshare.airline.master.georegion.eitities.TimezoneDLS;
import com.codeshare.airline.master.georegion.eitities.Timezone;
import com.codeshare.airline.master.georegion.repository.DstRuleRepository;
import com.codeshare.airline.master.georegion.repository.TimezoneRepository;
import com.codeshare.airline.master.georegion.service.DstRuleService;
import com.codeshare.airline.master.georegion.mappers.DstRuleMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DstRuleServiceImpl
        extends BaseServiceImpl<TimezoneDLS, DstRuleDTO, UUID>
        implements DstRuleService {

    private final TimezoneRepository timezoneRepository;

    public DstRuleServiceImpl(DstRuleRepository repository,
                              DstRuleMapper mapper,
                              TimezoneRepository timezoneRepository) {
        super(repository, mapper);
        this.timezoneRepository = timezoneRepository;
    }

    private Timezone getTimezone(UUID timezoneId) {
        return timezoneRepository.findById(timezoneId)
                .orElseThrow(() -> new EntityNotFoundException("Timezone not found"));
    }

    @Override
    public DstRuleDTO create(DstRuleDTO dto) {

        Timezone timezone = getTimezone(dto.getTimezoneId());

        TimezoneDLS rule = mapper.toEntity(dto);
        rule.setTimezone(timezone);

        return mapper.toDTO(repository.save(rule));
    }

    @Override
    public DstRuleDTO update(UUID id, DstRuleDTO dto) {

        TimezoneDLS existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DST Rule not found"));

        Timezone timezone = getTimezone(dto.getTimezoneId());

        mapper.updateEntityFromDto(dto, existing);
        existing.setTimezone(timezone);

        return mapper.toDTO(repository.save(existing));
    }
}