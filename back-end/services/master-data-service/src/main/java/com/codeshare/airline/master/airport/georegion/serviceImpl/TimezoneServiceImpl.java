package com.codeshare.airline.master.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.TimezoneDTO;
import com.codeshare.airline.master.airport.georegion.eitities.DstRule;
import com.codeshare.airline.master.airport.georegion.eitities.Timezone;
import com.codeshare.airline.master.airport.georegion.repository.DstRuleRepository;
import com.codeshare.airline.master.airport.georegion.repository.TimezoneRepository;
import com.codeshare.airline.master.airport.georegion.service.TimezoneService;
import com.codeshare.airline.master.airport.georegion.mappers.TimezoneMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
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