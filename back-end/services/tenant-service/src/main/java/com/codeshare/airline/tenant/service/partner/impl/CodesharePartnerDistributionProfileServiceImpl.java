package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerDistributionProfile;
import com.codeshare.airline.tenant.integration.master.MasterDataAirlineClient;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerDistributionProfileMapper;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerDistributionProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerDistributionProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerDistributionProfileServiceImpl implements CodesharePartnerDistributionProfileService {

    private final CodesharePartnerDistributionProfileRepository repository;
    private final CodesharePartnerDistributionProfileMapper mapper;
    private final CodesharePartnerRepository partnerRepository;
    private final TenantRepository tenantRepository;
    private final MasterDataAirlineClient masterDataAirlineClient;

    @Override
    public CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto) {
        CodesharePartnerDistributionProfile entity = mapper.toEntity(dto);
        entity.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerDistributionProfileDTO update(Long id, CodesharePartnerDistributionProfileDTO dto) {
        CodesharePartnerDistributionProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner distribution profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerDistributionProfileDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner distribution profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerDistributionProfileDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerDistributionProfileDTO> resolve(String tenantCode, String partnerCode, MessageType messageType) {
        CodesharePartner partner = resolvePartner(tenantCode, partnerCode);
        return mapper.toDTOList(repository.findEffectiveProfiles(partner.getId(), messageType, LocalDate.now()));
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
}
