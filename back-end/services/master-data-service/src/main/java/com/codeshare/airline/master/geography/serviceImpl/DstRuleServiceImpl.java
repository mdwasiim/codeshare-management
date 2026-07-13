package com.codeshare.airline.master.geography.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.georegion.DstRuleDTO;
import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.DstRuleRepository;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import com.codeshare.airline.master.geography.service.DstRuleService;
import com.codeshare.airline.master.geography.mappers.DstRuleMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class DstRuleServiceImpl
        extends BaseServiceImpl<TimezoneDLS, DstRuleDTO, Long>
        implements DstRuleService {

    private final TimezoneRepository timezoneRepository;

    public DstRuleServiceImpl(DstRuleRepository repository,
                              DstRuleMapper mapper,
                              TimezoneRepository timezoneRepository) {
        super(repository, mapper);
        this.timezoneRepository = timezoneRepository;
    }

    private Timezone getTimezone(Long timezoneId) {
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
    public DstRuleDTO update(Long id, DstRuleDTO dto) {

        TimezoneDLS existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DST Rule not found"));

        Timezone timezone = getTimezone(dto.getTimezoneId());

        mapper.updateEntityFromDto(dto, existing);
        existing.setTimezone(timezone);

        return mapper.toDTO(repository.save(existing));
    }
}