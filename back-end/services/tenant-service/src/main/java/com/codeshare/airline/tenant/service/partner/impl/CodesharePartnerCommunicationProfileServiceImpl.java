package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;
import com.codeshare.airline.tenant.integration.master.MasterDataAirlineClient;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerCommunicationProfileMapper;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerCommunicationProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerCommunicationProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerCommunicationProfileServiceImpl implements CodesharePartnerCommunicationProfileService {

    private final CodesharePartnerCommunicationProfileRepository repository;
    private final CodesharePartnerCommunicationProfileMapper mapper;
    private final CodesharePartnerRepository partnerRepository;
    private final TenantRepository tenantRepository;
    private final MasterDataAirlineClient masterDataAirlineClient;

    @Override
    public CodesharePartnerCommunicationProfileDTO create(CodesharePartnerCommunicationProfileDTO dto) {
        CodesharePartnerCommunicationProfile entity = mapper.toEntity(dto);
        entity.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerCommunicationProfileDTO update(Long id, CodesharePartnerCommunicationProfileDTO dto) {
        CodesharePartnerCommunicationProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner communication profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerCommunicationProfileDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner communication profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerCommunicationProfileDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerCommunicationProfileDTO> getCurrent(String tenantCode) {
        Long tenantId = resolveTenantId(tenantCode);
        return mapper.toDTOList(repository.findByPartner_TenantIdOrderByPartner_IdAscDisplayOrderAscIdAsc(tenantId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerCommunicationProfileDTO> resolve(
            String tenantCode,
            String partnerCode,
            CommunicationProtocol protocol
    ) {
        CodesharePartner partner = resolvePartner(tenantCode, partnerCode);
        return mapper.toDTOList(repository.findEffectiveProfiles(partner.getId(), protocol, LocalDate.now()));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private CodesharePartner resolvePartner(String tenantCode, String partnerCode) {
        Tenant tenant = tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
        AirlineCarrierDTO homeAirline = masterDataAirlineClient.getByIataCode(tenant.getTenantCode());
        AirlineCarrierDTO partnerAirline = masterDataAirlineClient.getByIataCode(partnerCode);
        return partnerRepository.findByTenantIdAndHomeAirlineIdAndPartnerAirlineId(
                        tenant.getId(),
                        homeAirline.getId(),
                        partnerAirline.getId()
                )
                .orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    private Long resolveTenantId(String tenantCode) {
        if (tenantCode == null || tenantCode.isBlank()) {
            throw new IllegalArgumentException("Tenant header is required");
        }

        return tenantRepository.findByTenantCode(tenantCode.trim().toUpperCase())
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + tenantCode))
                .getId();
    }
}
