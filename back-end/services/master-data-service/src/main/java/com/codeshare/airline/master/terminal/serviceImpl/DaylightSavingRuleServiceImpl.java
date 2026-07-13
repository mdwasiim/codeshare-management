package com.codeshare.airline.master.terminal.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.terminal.DaylightSavingRuleDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import com.codeshare.airline.master.terminal.entities.DaylightSavingRule;
import com.codeshare.airline.master.terminal.mappers.DaylightSavingRuleMapper;
import com.codeshare.airline.master.terminal.repository.DaylightSavingRuleRepository;
import com.codeshare.airline.master.terminal.service.DaylightSavingRuleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class DaylightSavingRuleServiceImpl
        extends BaseServiceImpl<DaylightSavingRule, DaylightSavingRuleDTO, Long>
        implements DaylightSavingRuleService {

    private final TimezoneRepository timezoneRepository;

    public DaylightSavingRuleServiceImpl(DaylightSavingRuleRepository repository,
                                         DaylightSavingRuleMapper mapper,
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
    public DaylightSavingRuleDTO create(DaylightSavingRuleDTO dto) {
        DaylightSavingRule entity = mapper.toEntity(dto);
        entity.setTimezone(getTimezone(dto.getTimezoneId()));

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public DaylightSavingRuleDTO update(Long id, DaylightSavingRuleDTO dto) {
        DaylightSavingRule existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Daylight saving rule not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setTimezone(getTimezone(dto.getTimezoneId()));

        return mapper.toDTO(repository.save(existing));
    }
}
