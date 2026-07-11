package com.codeshare.airline.tenant.mappers.partner;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CodesharePartnerProfileMapper implements CSMGenericMapper<CodesharePartnerProfile, CodesharePartnerProfileDTO> {

    @Override
    public CodesharePartnerProfileDTO toDTO(CodesharePartnerProfile entity) {
        if (entity == null) {
            return null;
        }

        CodesharePartnerProfileDTO dto = new CodesharePartnerProfileDTO();
        dto.setId(entity.getId());
        dto.setPartnerId(entity.getPartner() != null ? entity.getPartner().getId() : null);
        dto.setProfileCode(entity.getProfileCode());
        dto.setProfileName(entity.getProfileName());
        dto.setPartnerType(entity.getPartnerType());
        dto.setAgreementCategory(entity.getAgreementCategory());
        dto.setInventorySharingType(entity.getInventorySharingType());
        dto.setPriority(entity.getPriority());
        dto.setAutoAcceptScheduleChanges(entity.getAutoAcceptScheduleChanges());
        dto.setProrationApplicable(entity.getProrationApplicable());
        dto.setETicketAllowed(entity.getETicketAllowed());
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
    public CodesharePartnerProfile toEntity(CodesharePartnerProfileDTO dto) {
        if (dto == null) {
            return null;
        }

        CodesharePartnerProfile entity = new CodesharePartnerProfile();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    @Override
    public void updateEntityFromDto(CodesharePartnerProfileDTO dto, CodesharePartnerProfile entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setProfileCode(dto.getProfileCode());
        entity.setProfileName(dto.getProfileName());
        entity.setPartnerType(dto.getPartnerType());
        entity.setAgreementCategory(dto.getAgreementCategory());
        entity.setInventorySharingType(dto.getInventorySharingType());
        entity.setPriority(dto.getPriority());
        entity.setAutoAcceptScheduleChanges(dto.getAutoAcceptScheduleChanges());
        entity.setProrationApplicable(dto.getProrationApplicable());
        entity.setETicketAllowed(dto.getETicketAllowed());
        entity.setActive(dto.getActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setDescription(dto.getDescription());
        entity.setRemarks(dto.getRemarks());
        entity.setRecordStatus(dto.getRecordStatus());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
    }

    @Override
    public List<CodesharePartnerProfileDTO> toDTOList(List<CodesharePartnerProfile> list) {
        return list == null ? List.of() : list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerProfileDTO> toDTOSet(Set<CodesharePartnerProfile> set) {
        return set == null ? Set.of() : set.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @Override
    public List<CodesharePartnerProfile> toEntityList(List<CodesharePartnerProfileDTO> list) {
        return list == null ? List.of() : list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerProfile> toEntitySet(Set<CodesharePartnerProfileDTO> set) {
        return set == null ? Set.of() : set.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
