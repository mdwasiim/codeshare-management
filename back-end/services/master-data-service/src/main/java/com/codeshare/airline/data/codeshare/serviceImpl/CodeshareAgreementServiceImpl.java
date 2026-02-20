package com.codeshare.airline.data.codeshare.serviceImpl;

import com.codeshare.airline.core.dto.codeshare.CodeshareAgreementDTO;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.data.core.repository.AirlineCarrierRepository;
import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import com.codeshare.airline.data.codeshare.repository.CodeshareAgreementRepository;
import com.codeshare.airline.data.codeshare.service.CodeshareAgreementService;
import com.codeshare.airline.data.codeshare.utils.mappers.CodeshareAgreementMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeshareAgreementServiceImpl
        extends BaseServiceImpl<CodeshareAgreement, CodeshareAgreementDTO, UUID>
        implements CodeshareAgreementService {

    private final CodeshareAgreementRepository repository;
    private final AirlineCarrierRepository airlineRepository;

    public CodeshareAgreementServiceImpl(
            CodeshareAgreementRepository repository,
            CodeshareAgreementMapper mapper,
            AirlineCarrierRepository airlineRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.airlineRepository = airlineRepository;
    }

    private AirlineCarrier getAirline(UUID id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    private void validateAgreement(CodeshareAgreementDTO dto) {

        if (dto.getMarketingAirlineId().equals(dto.getOperatingAirlineId())) {
            throw new IllegalStateException(
                    "Marketing and operating airline cannot be the same."
            );
        }

        if (repository.existsByMarketingAirlineIdAndOperatingAirlineIdAndEffectiveFrom(
                dto.getMarketingAirlineId(),
                dto.getOperatingAirlineId(),
                dto.getEffectiveFrom())) {

            throw new IllegalStateException(
                    "Codeshare agreement already exists for this date."
            );
        }
    }

    @Override
    public CodeshareAgreementDTO create(CodeshareAgreementDTO dto) {

        validateAgreement(dto);

        AirlineCarrier marketing = getAirline(dto.getMarketingAirlineId());
        AirlineCarrier operating = getAirline(dto.getOperatingAirlineId());

        CodeshareAgreement agreement = mapper.toEntity(dto);
        agreement.setMarketingAirline(marketing);
        agreement.setOperatingAirline(operating);

        return mapper.toDTO(repository.save(agreement));
    }

    @Override
    public CodeshareAgreementDTO update(UUID id,
                                        CodeshareAgreementDTO dto) {

        CodeshareAgreement existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Agreement not found"));

        AirlineCarrier marketing = getAirline(dto.getMarketingAirlineId());
        AirlineCarrier operating = getAirline(dto.getOperatingAirlineId());

        mapper.updateEntityFromDto(dto, existing);
        existing.setMarketingAirline(marketing);
        existing.setOperatingAirline(operating);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<CodeshareAgreementDTO> getByMarketingAirline(UUID airlineId) {
        return mapper.toDTOList(repository.findByMarketingAirlineId(airlineId));
    }

    @Override
    public List<CodeshareAgreementDTO> getByOperatingAirline(UUID airlineId) {
        return mapper.toDTOList(repository.findByOperatingAirlineId(airlineId));
    }
}