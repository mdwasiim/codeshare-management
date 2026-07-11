package com.codeshare.airline.tenant.mappers.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerDistributionProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CodesharePartnerDistributionProfileMapper implements CSMGenericMapper<CodesharePartnerDistributionProfile, CodesharePartnerDistributionProfileDTO> {

    @Override
    public CodesharePartnerDistributionProfileDTO toDTO(CodesharePartnerDistributionProfile entity) {
        if (entity == null) {
            return null;
        }

        CodesharePartnerDistributionProfileDTO dto = new CodesharePartnerDistributionProfileDTO();
        dto.setId(entity.getId());
        dto.setPartnerId(entity.getPartner() != null ? entity.getPartner().getId() : null);
        dto.setProfileCode(entity.getProfileCode());
        dto.setProfileName(entity.getProfileName());
        dto.setDistributionChannel(entity.getDistributionChannel());
        dto.setDistributionMode(entity.getDistributionMode());
        dto.setMessageType(entity.getMessageType());
        dto.setRealTimeEnabled(entity.getRealTimeEnabled());
        dto.setAcknowledgementRequired(entity.getAcknowledgementRequired());
        dto.setRetryEnabled(entity.getRetryEnabled());
        dto.setRetryCount(entity.getRetryCount());
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
    public CodesharePartnerDistributionProfile toEntity(CodesharePartnerDistributionProfileDTO dto) {
        if (dto == null) {
            return null;
        }

        CodesharePartnerDistributionProfile entity = new CodesharePartnerDistributionProfile();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    @Override
    public void updateEntityFromDto(CodesharePartnerDistributionProfileDTO dto, CodesharePartnerDistributionProfile entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setProfileCode(dto.getProfileCode());
        entity.setProfileName(dto.getProfileName());
        entity.setDistributionChannel(dto.getDistributionChannel());
        entity.setDistributionMode(dto.getDistributionMode());
        entity.setMessageType(dto.getMessageType());
        entity.setRealTimeEnabled(dto.getRealTimeEnabled());
        entity.setAcknowledgementRequired(dto.getAcknowledgementRequired());
        entity.setRetryEnabled(dto.getRetryEnabled());
        entity.setRetryCount(dto.getRetryCount());
        entity.setActive(dto.getActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setDescription(dto.getDescription());
        entity.setRemarks(dto.getRemarks());
        entity.setRecordStatus(dto.getRecordStatus());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
    }

    @Override
    public List<CodesharePartnerDistributionProfileDTO> toDTOList(List<CodesharePartnerDistributionProfile> list) {
        return list == null ? List.of() : list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerDistributionProfileDTO> toDTOSet(Set<CodesharePartnerDistributionProfile> set) {
        return set == null ? Set.of() : set.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @Override
    public List<CodesharePartnerDistributionProfile> toEntityList(List<CodesharePartnerDistributionProfileDTO> list) {
        return list == null ? List.of() : list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerDistributionProfile> toEntitySet(Set<CodesharePartnerDistributionProfileDTO> set) {
        return set == null ? Set.of() : set.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
