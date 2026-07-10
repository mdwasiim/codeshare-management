package com.codeshare.airline.tenant.mappers.partner;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CodesharePartnerCommunicationProfileMapper implements CSMGenericMapper<CodesharePartnerCommunicationProfile, CodesharePartnerCommunicationProfileDTO> {

    @Override
    public CodesharePartnerCommunicationProfileDTO toDTO(CodesharePartnerCommunicationProfile entity) {
        if (entity == null) {
            return null;
        }

        CodesharePartnerCommunicationProfileDTO dto = new CodesharePartnerCommunicationProfileDTO();
        dto.setId(entity.getId());
        dto.setPartnerId(entity.getPartner() != null ? entity.getPartner().getId() : null);
        dto.setProfileCode(entity.getProfileCode());
        dto.setProfileName(entity.getProfileName());
        dto.setProtocol(entity.getProtocol());
        dto.setTransportType(entity.getTransportType());
        dto.setMessageFormat(entity.getMessageFormat());
        dto.setAuthenticationType(entity.getAuthenticationType());
        dto.setEndpointUrl(entity.getEndpointUrl());
        dto.setUsername(entity.getUsername());
        dto.setCredentialAlias(entity.getCredentialAlias());
        dto.setConnectionTimeout(entity.getConnectionTimeout());
        dto.setReadTimeout(entity.getReadTimeout());
        dto.setRetryCount(entity.getRetryCount());
        dto.setCompressionEnabled(entity.getCompressionEnabled());
        dto.setEncryptionEnabled(entity.getEncryptionEnabled());
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
    public CodesharePartnerCommunicationProfile toEntity(CodesharePartnerCommunicationProfileDTO dto) {
        if (dto == null) {
            return null;
        }

        CodesharePartnerCommunicationProfile entity = new CodesharePartnerCommunicationProfile();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    @Override
    public void updateEntityFromDto(CodesharePartnerCommunicationProfileDTO dto, CodesharePartnerCommunicationProfile entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setProfileCode(dto.getProfileCode());
        entity.setProfileName(dto.getProfileName());
        entity.setProtocol(dto.getProtocol());
        entity.setTransportType(dto.getTransportType());
        entity.setMessageFormat(dto.getMessageFormat());
        entity.setAuthenticationType(dto.getAuthenticationType());
        entity.setEndpointUrl(dto.getEndpointUrl());
        entity.setUsername(dto.getUsername());
        entity.setCredentialAlias(dto.getCredentialAlias());
        entity.setConnectionTimeout(dto.getConnectionTimeout());
        entity.setReadTimeout(dto.getReadTimeout());
        entity.setRetryCount(dto.getRetryCount());
        entity.setCompressionEnabled(dto.getCompressionEnabled());
        entity.setEncryptionEnabled(dto.getEncryptionEnabled());
        entity.setActive(dto.getActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setDescription(dto.getDescription());
        entity.setRemarks(dto.getRemarks());
        entity.setRecordStatus(dto.getRecordStatus());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
    }

    @Override
    public List<CodesharePartnerCommunicationProfileDTO> toDTOList(List<CodesharePartnerCommunicationProfile> list) {
        return list == null ? List.of() : list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerCommunicationProfileDTO> toDTOSet(Set<CodesharePartnerCommunicationProfile> set) {
        return set == null ? Set.of() : set.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @Override
    public List<CodesharePartnerCommunicationProfile> toEntityList(List<CodesharePartnerCommunicationProfileDTO> list) {
        return list == null ? List.of() : list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerCommunicationProfile> toEntitySet(Set<CodesharePartnerCommunicationProfileDTO> set) {
        return set == null ? Set.of() : set.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
