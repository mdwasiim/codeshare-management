package com.codeshare.airline.master.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.DstRuleDTO;
import com.codeshare.airline.master.airport.georegion.eitities.DstRule;
import com.codeshare.airline.master.airport.georegion.eitities.Timezone;
import com.codeshare.airline.master.airport.georegion.repository.DstRuleRepository;
import com.codeshare.airline.master.airport.georegion.repository.TimezoneRepository;
import com.codeshare.airline.master.airport.georegion.service.DstRuleService;
import com.codeshare.airline.master.airport.georegion.mappers.DstRuleMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DstRuleServiceImpl
        extends BaseServiceImpl<DstRule, DstRuleDTO, UUID>
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

        DstRule rule = mapper.toEntity(dto);
        rule.setTimezone(timezone);

        return mapper.toDTO(repository.save(rule));
    }

    @Override
    public DstRuleDTO update(UUID id, DstRuleDTO dto) {

        DstRule existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DST Rule not found"));

        Timezone timezone = getTimezone(dto.getTimezoneId());

        mapper.updateEntityFromDto(dto, existing);
        existing.setTimezone(timezone);

        return mapper.toDTO(repository.save(existing));
    }
}