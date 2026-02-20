package com.codeshare.airline.data.codeshare.serviceImpl;

import com.codeshare.airline.core.dto.codeshare.CodeshareRouteDTO;
import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import com.codeshare.airline.data.codeshare.eitities.CodeshareRoute;
import com.codeshare.airline.data.codeshare.repository.CodeshareAgreementRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareRouteRepository;
import com.codeshare.airline.data.codeshare.service.CodeshareRouteService;
import com.codeshare.airline.data.codeshare.utils.mappers.CodeshareRouteMapper;
import com.codeshare.airline.data.core.eitities.Airport;
import com.codeshare.airline.data.core.repository.AirportRepository;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeshareRouteServiceImpl
        extends BaseServiceImpl<CodeshareRoute, CodeshareRouteDTO, UUID>
        implements CodeshareRouteService {

    private final CodeshareRouteRepository repository;
    private final CodeshareAgreementRepository agreementRepository;
    private final AirportRepository airportRepository;
    private final CodeshareRouteMapper mapper;

    public CodeshareRouteServiceImpl(
            CodeshareRouteRepository repository,
            CodeshareRouteMapper mapper,
            CodeshareAgreementRepository agreementRepository,
            AirportRepository airportRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.agreementRepository = agreementRepository;
        this.airportRepository = airportRepository;
    }

    private CodeshareAgreement getAgreement(UUID id) {
        return agreementRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Agreement not found"));
    }

    private Airport getAirport(UUID id) {
        return airportRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Airport not found"));
    }

    @Override
    public CodeshareRouteDTO create(CodeshareRouteDTO dto) {

        // Validate duplicate seasonal route
        if (repository.existsByAgreementIdAndOriginIdAndDestinationIdAndEffectiveFrom(
                dto.getAgreementId(),
                dto.getOriginId(),
                dto.getDestinationId(),
                dto.getEffectiveFrom())) {

            throw new IllegalStateException(
                    "Route already exists for this effective date."
            );
        }

        CodeshareAgreement agreement = getAgreement(dto.getAgreementId());
        Airport origin = getAirport(dto.getOriginId());
        Airport destination = getAirport(dto.getDestinationId());

        if (origin.getId().equals(destination.getId())) {
            throw new IllegalStateException(
                    "Origin and destination cannot be the same."
            );
        }

        // Reverse check if bidirectional
        if (Boolean.TRUE.equals(dto.getBidirectional())) {

            boolean reverseExists =
                    repository.existsByAgreementIdAndOriginIdAndDestinationIdAndEffectiveFrom(
                            dto.getAgreementId(),
                            dto.getDestinationId(),
                            dto.getOriginId(),
                            dto.getEffectiveFrom());

            if (reverseExists) {
                throw new IllegalStateException(
                        "Reverse route already exists."
                );
            }
        }

        CodeshareRoute entity = mapper.toEntity(dto);
        entity.setAgreement(agreement);
        entity.setOrigin(origin);
        entity.setDestination(destination);

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodeshareRouteDTO update(UUID id, CodeshareRouteDTO dto) {

        CodeshareRoute existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Route not found"));

        Airport origin = getAirport(dto.getOriginId());
        Airport destination = getAirport(dto.getDestinationId());

        if (origin.getId().equals(destination.getId())) {
            throw new IllegalStateException(
                    "Origin and destination cannot be the same."
            );
        }

        mapper.updateEntityFromDto(dto, existing);

        existing.setOrigin(origin);
        existing.setDestination(destination);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<CodeshareRouteDTO> getByAgreement(UUID agreementId) {

        return mapper.toDTOList(
                repository.findByAgreementId(agreementId)
        );
    }
}