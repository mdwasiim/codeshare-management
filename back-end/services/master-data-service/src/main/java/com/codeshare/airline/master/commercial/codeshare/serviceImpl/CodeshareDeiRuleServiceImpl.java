package com.codeshare.airline.master.commercial.codeshare.serviceImpl;

import com.codeshare.airline.dto.codeshare.CodeshareDeiRuleDTO;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareDeiRule;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.master.commercial.codeshare.repository.CodeshareDeiRuleRepository;
import com.codeshare.airline.master.commercial.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.master.commercial.codeshare.service.CodeshareDeiRuleService;
import com.codeshare.airline.master.commercial.codeshare.utils.mappers.CodeshareDeiRuleMapper;
import com.codeshare.airline.messaging.eitities.DeiRegistry;
import com.codeshare.airline.messaging.repository.DeiRepository;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeshareDeiRuleServiceImpl
        extends BaseServiceImpl<CodeshareDeiRule, CodeshareDeiRuleDTO, UUID>
        implements CodeshareDeiRuleService {

    private final CodeshareDeiRuleRepository repository;
    private final CodeshareFlightMappingRepository flightMappingRepository;
    private final DeiRepository deiRepository;
    private final CodeshareDeiRuleMapper mapper;

    public CodeshareDeiRuleServiceImpl(
            CodeshareDeiRuleRepository repository,
            CodeshareDeiRuleMapper mapper,
            CodeshareFlightMappingRepository flightMappingRepository,
            DeiRepository deiRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.flightMappingRepository = flightMappingRepository;
        this.deiRepository = deiRepository;
    }

    private CodeshareFlightMapping getFlightMapping(UUID id) {
        return flightMappingRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Flight mapping not found"));
    }

    private DeiRegistry getDei(UUID id) {
        return deiRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("DEI not found"));
    }

    @Override
    public CodeshareDeiRuleDTO create(CodeshareDeiRuleDTO dto) {

        // Prevent duplicate seasonal rule
        if (repository.existsByFlightMappingIdAndDeiIdAndEffectiveFrom(
                dto.getFlightMappingId(),
                dto.getDeiId(),
                dto.getEffectiveFrom())) {

            throw new IllegalStateException(
                    "DEI rule already exists for this effective date."
            );
        }

        CodeshareFlightMapping flightMapping =
                getFlightMapping(dto.getFlightMappingId());

        DeiRegistry deiRegistry = getDei(dto.getDeiId());

        CodeshareDeiRule entity = mapper.toEntity(dto);
        entity.setFlightMapping(flightMapping);
        entity.setDei(deiRegistry);

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodeshareDeiRuleDTO update(UUID id, CodeshareDeiRuleDTO dto) {

        CodeshareDeiRule existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("DEI rule not found"));

        CodeshareFlightMapping flightMapping =
                getFlightMapping(dto.getFlightMappingId());

        DeiRegistry deiRegistry = getDei(dto.getDeiId());

        mapper.updateEntityFromDto(dto, existing);

        existing.setFlightMapping(flightMapping);
        existing.setDei(deiRegistry);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<CodeshareDeiRuleDTO> getByFlightMapping(UUID flightMappingId) {

        return mapper.toDTOList(
                repository.findByFlightMappingId(flightMappingId)
        );
    }
}