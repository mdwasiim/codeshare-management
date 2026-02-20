package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.TimezoneDTO;
import com.codeshare.airline.data.core.eitities.DstRule;
import com.codeshare.airline.data.core.eitities.Timezone;
import com.codeshare.airline.data.core.repository.DstRuleRepository;
import com.codeshare.airline.data.core.repository.TimezoneRepository;
import com.codeshare.airline.data.core.service.TimezoneService;
import com.codeshare.airline.data.core.utils.mappers.TimezoneMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TimezoneServiceImpl
        extends BaseServiceImpl<Timezone, TimezoneDTO, UUID>
        implements TimezoneService {

    private final DstRuleRepository dstRuleRepository;

    public TimezoneServiceImpl(TimezoneRepository repository,
                               TimezoneMapper mapper,
                               DstRuleRepository dstRuleRepository) {
        super(repository, mapper);
        this.dstRuleRepository = dstRuleRepository;
    }

    private DstRule getDstRule(UUID dstRuleId) {
        return dstRuleRepository.findById(dstRuleId)
                .orElseThrow(() -> new EntityNotFoundException("DST Rule not found"));
    }

    @Override
    public TimezoneDTO create(TimezoneDTO dto) {

        Timezone timezone = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(timezone));
    }

    @Override
    public TimezoneDTO update(UUID id, TimezoneDTO dto) {

        Timezone existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Timezone not found"));

        mapper.updateEntityFromDto(dto, existing);

        return mapper.toDTO(repository.save(existing));
    }
}