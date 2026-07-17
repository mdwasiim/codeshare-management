package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerAcceptanceRuleDTO;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerAcceptanceRule;
import com.codeshare.airline.tenant.integration.master.MasterDataAirlineClient;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerAcceptanceRuleMapper;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerAcceptanceRuleRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerAcceptanceRuleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerAcceptanceRuleServiceImpl implements CodesharePartnerAcceptanceRuleService {

    private final CodesharePartnerAcceptanceRuleRepository repository;
    private final CodesharePartnerRepository partnerRepository;
    private final CodesharePartnerProfileRepository profileRepository;
    private final TenantRepository tenantRepository;
    private final MasterDataAirlineClient masterDataAirlineClient;
    private final CodesharePartnerAcceptanceRuleMapper mapper;

    @Override
    public CodesharePartnerAcceptanceRuleDTO create(CodesharePartnerAcceptanceRuleDTO dto) {
        CodesharePartnerAcceptanceRule entity = mapper.toEntity(dto);
        entity.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerAcceptanceRuleDTO update(Long id, CodesharePartnerAcceptanceRuleDTO dto) {
        CodesharePartnerAcceptanceRule existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner acceptance rule not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerAcceptanceRuleDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner acceptance rule not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerAcceptanceRuleDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerAcceptanceRuleDTO resolve(String tenantCode, String partnerCode, MessageType messageType) {
        CodesharePartner partner = resolvePartner(tenantCode, partnerCode);
        LocalDate businessDate = LocalDate.now();

        return repository.findEffectiveRule(partner.getId(), messageType, businessDate)
                .map(mapper::toDTO)
                .map(dto -> enrichLookup(dto, tenantCode, partnerCode))
                .orElseGet(() -> fallbackRule(partner, tenantCode, partnerCode, messageType, businessDate));
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

    private CodesharePartnerAcceptanceRuleDTO fallbackRule(
            CodesharePartner partner,
            String tenantCode,
            String partnerCode,
            MessageType messageType,
            LocalDate businessDate
    ) {
        ApprovalMode approvalMode = profileRepository.findEffectiveProfile(partner.getId(), businessDate)
                .filter(profile -> Boolean.TRUE.equals(profile.getAutoAcceptScheduleChanges()))
                .map(profile -> ApprovalMode.AUTO)
                .orElse(ApprovalMode.MANUAL);

        CodesharePartnerAcceptanceRuleDTO dto = new CodesharePartnerAcceptanceRuleDTO();
        dto.setPartnerId(partner.getId());
        dto.setTenantCode(tenantCode);
        dto.setPartnerCode(partnerCode);
        dto.setMessageType(messageType);
        dto.setApprovalMode(approvalMode);
        dto.setActive(Boolean.TRUE);
        dto.setRecordStatus(RecordStatus.ACTIVE);
        return dto;
    }

    private CodesharePartnerAcceptanceRuleDTO enrichLookup(CodesharePartnerAcceptanceRuleDTO dto, String tenantCode, String partnerCode) {
        dto.setTenantCode(tenantCode);
        dto.setPartnerCode(partnerCode);
        return dto;
    }
}
