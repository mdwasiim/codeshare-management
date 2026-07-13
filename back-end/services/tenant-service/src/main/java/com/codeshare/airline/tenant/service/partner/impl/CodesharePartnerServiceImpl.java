package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.integration.master.MasterDataAirlineClient;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerMapper;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerServiceImpl implements CodesharePartnerService {

    private final CodesharePartnerRepository repository;
    private final CodesharePartnerMapper mapper;
    private final MasterDataAirlineClient masterDataAirlineClient;

    @Override
    public CodesharePartnerDTO create(CodesharePartnerDTO dto) {
        return enrich(mapper.toDTO(repository.save(mapper.toEntity(dto))), new HashMap<>());
    }

    @Override
    public CodesharePartnerDTO update(Long id, CodesharePartnerDTO dto) {
        CodesharePartner existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
        mapper.updateEntityFromDto(dto, existing);
        return enrich(mapper.toDTO(repository.save(existing)), new HashMap<>());
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerDTO getById(Long id) {
        Map<Long, AirlineCarrierDTO> airlineCache = new HashMap<>();
        return repository.findById(id)
                .map(mapper::toDTO)
                .map(dto -> enrich(dto, airlineCache))
                .orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerDTO> getAll() {
        Map<Long, AirlineCarrierDTO> airlineCache = new HashMap<>();
        return mapper.toDTOList(repository.findAll()).stream()
                .map(dto -> enrich(dto, airlineCache))
                .toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private CodesharePartnerDTO enrich(CodesharePartnerDTO dto, Map<Long, AirlineCarrierDTO> airlineCache) {
        if (dto == null) {
            return null;
        }

        AirlineCarrierDTO homeAirline = resolveAirline(dto.getHomeAirlineId(), airlineCache);
        if (homeAirline != null) {
            dto.setHomeAirlineCode(homeAirline.getIataCode());
            dto.setHomeAirlineName(resolveDisplayName(homeAirline));
        }

        AirlineCarrierDTO partnerAirline = resolveAirline(dto.getPartnerAirlineId(), airlineCache);
        if (partnerAirline != null) {
            dto.setPartnerAirlineCode(partnerAirline.getIataCode());
            dto.setPartnerAirlineName(resolveDisplayName(partnerAirline));
        }

        return dto;
    }

    private AirlineCarrierDTO resolveAirline(Long airlineId, Map<Long, AirlineCarrierDTO> airlineCache) {
        if (airlineId == null) {
            return null;
        }
        if (airlineCache.containsKey(airlineId)) {
            return airlineCache.get(airlineId);
        }

        try {
            AirlineCarrierDTO airline = masterDataAirlineClient.getById(airlineId);
            airlineCache.put(airlineId, airline);
            return airline;
        } catch (RuntimeException ex) {
            log.warn("Unable to enrich airline {} for codeshare partner display", airlineId);
            airlineCache.put(airlineId, null);
            return null;
        }
    }

    private String resolveDisplayName(AirlineCarrierDTO airline) {
        if (airline == null) {
            return null;
        }
        if (airline.getDisplayName() != null && !airline.getDisplayName().isBlank()) {
            return airline.getDisplayName();
        }
        if (airline.getCommercialName() != null && !airline.getCommercialName().isBlank()) {
            return airline.getCommercialName();
        }
        return airline.getLegalName();
    }
}
