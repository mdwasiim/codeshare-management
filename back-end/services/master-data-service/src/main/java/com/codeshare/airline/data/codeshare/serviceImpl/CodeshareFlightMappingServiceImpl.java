package com.codeshare.airline.data.codeshare.serviceImpl;

import com.codeshare.airline.core.dto.codeshare.CodeshareFlightMappingDTO;
import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.data.codeshare.repository.CodeshareAgreementRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.data.codeshare.service.CodeshareFlightMappingService;
import com.codeshare.airline.data.codeshare.utils.mappers.CodeshareFlightMappingMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeshareFlightMappingServiceImpl
        extends BaseServiceImpl<CodeshareFlightMapping, CodeshareFlightMappingDTO, UUID>
        implements CodeshareFlightMappingService {

    private final CodeshareFlightMappingRepository repository;
    private final CodeshareAgreementRepository agreementRepository;

    public CodeshareFlightMappingServiceImpl(
            CodeshareFlightMappingRepository repository,
            CodeshareFlightMappingMapper mapper,
            CodeshareAgreementRepository agreementRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.agreementRepository = agreementRepository;
    }

    private CodeshareAgreement getAgreement(UUID id) {
        return agreementRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Agreement not found"));
    }

    @Override
    public CodeshareFlightMappingDTO create(CodeshareFlightMappingDTO dto) {

        if (repository.existsByAgreementIdAndOperatingFlightNumberAndMarketingFlightNumberAndEffectiveFrom(
                dto.getAgreementId(),
                dto.getOperatingFlightNumber(),
                dto.getMarketingFlightNumber(),
                dto.getEffectiveFrom())) {

            throw new IllegalStateException(
                    "Flight mapping already exists for this date."
            );
        }


        CodeshareFlightMapping mapping = mapper.toEntity(dto);

        return mapper.toDTO(repository.save(mapping));
    }

    @Override
    public List<CodeshareFlightMappingDTO> getByAgreement(UUID agreementId) {
        return mapper.toDTOList(
                repository.findByAgreementId(agreementId)
        );
    }
}