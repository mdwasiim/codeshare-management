package com.codeshare.airline.tenant.mappers.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerAcceptanceRuleDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerAcceptanceRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CodesharePartnerAcceptanceRuleMapper implements CSMGenericMapper<CodesharePartnerAcceptanceRule, CodesharePartnerAcceptanceRuleDTO> {

    @Override
    public CodesharePartnerAcceptanceRuleDTO toDTO(CodesharePartnerAcceptanceRule entity) {
        if (entity == null) {
            return null;
        }

        CodesharePartnerAcceptanceRuleDTO dto = new CodesharePartnerAcceptanceRuleDTO();
        dto.setId(entity.getId());
        dto.setPartnerId(entity.getPartner() != null ? entity.getPartner().getId() : null);
        dto.setMessageType(entity.getMessageType());
        dto.setApprovalMode(entity.getApprovalMode());
        dto.setActive(entity.getActive());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setDescription(entity.getDescription());
        dto.setRemarks(entity.getRemarks());
        dto.setRecordStatus(entity.getRecordStatus());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setEffectiveTo(entity.getEffectiveTo());
        return dto;
    }

    @Override
    public CodesharePartnerAcceptanceRule toEntity(CodesharePartnerAcceptanceRuleDTO dto) {
        if (dto == null) {
            return null;
        }

        CodesharePartnerAcceptanceRule entity = new CodesharePartnerAcceptanceRule();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    @Override
    public void updateEntityFromDto(CodesharePartnerAcceptanceRuleDTO dto, CodesharePartnerAcceptanceRule entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setMessageType(dto.getMessageType());
        entity.setApprovalMode(dto.getApprovalMode());
        entity.setActive(dto.getActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setDescription(dto.getDescription());
        entity.setRemarks(dto.getRemarks());
        entity.setRecordStatus(dto.getRecordStatus());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
    }

    @Override
    public List<CodesharePartnerAcceptanceRuleDTO> toDTOList(List<CodesharePartnerAcceptanceRule> list) {
        return list == null ? List.of() : list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerAcceptanceRuleDTO> toDTOSet(Set<CodesharePartnerAcceptanceRule> set) {
        return set == null ? Set.of() : set.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @Override
    public List<CodesharePartnerAcceptanceRule> toEntityList(List<CodesharePartnerAcceptanceRuleDTO> list) {
        return list == null ? List.of() : list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerAcceptanceRule> toEntitySet(Set<CodesharePartnerAcceptanceRuleDTO> set) {
        return set == null ? Set.of() : set.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
