package com.codeshare.airline.data.codeshare.serviceImpl;

import com.codeshare.airline.core.dto.codeshare.CodeshareDayRuleDTO;
import com.codeshare.airline.data.codeshare.eitities.CodeshareDayRule;
import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.data.codeshare.repository.CodeshareDayRuleRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.data.codeshare.service.CodeshareDayRuleService;
import com.codeshare.airline.data.codeshare.utils.mappers.CodeshareDayRuleMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeshareDayRuleServiceImpl
        extends BaseServiceImpl<CodeshareDayRule, CodeshareDayRuleDTO, UUID>
        implements CodeshareDayRuleService {

    private final CodeshareDayRuleRepository repository;
    private final CodeshareFlightMappingRepository mappingRepository;

    public CodeshareDayRuleServiceImpl(
            CodeshareDayRuleRepository repository,
            CodeshareDayRuleMapper mapper,
            CodeshareFlightMappingRepository mappingRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.mappingRepository = mappingRepository;
    }

    private CodeshareFlightMapping getMapping(UUID id) {
        return mappingRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Flight mapping not found"));
    }

    @Override
    public CodeshareDayRuleDTO create(CodeshareDayRuleDTO dto) {

        if (repository.existsByFlightMappingIdAndEffectiveFrom(
                dto.getFlightMappingId(),
                dto.getEffectiveFrom())) {

            throw new IllegalStateException(
                    "Rule already exists for this effective date."
            );
        }

        CodeshareFlightMapping mapping =
                getMapping(dto.getFlightMappingId());

        CodeshareDayRule rule = mapper.toEntity(dto);
        rule.setFlightMapping(mapping);

        return mapper.toDTO(repository.save(rule));
    }

    @Override
    public List<CodeshareDayRuleDTO> getByFlightMapping(UUID mappingId) {
        return mapper.toDTOList(
                repository.findByFlightMappingId(mappingId)
        );
    }
}