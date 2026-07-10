package com.codeshare.airline.tenant.mappers.partner;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CodesharePartnerMapper implements CSMGenericMapper<CodesharePartner, CodesharePartnerDTO> {

    @Override
    public CodesharePartnerDTO toDTO(CodesharePartner entity) {
        if (entity == null) {
            return null;
        }

        CodesharePartnerDTO dto = new CodesharePartnerDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setHomeAirlineId(entity.getHomeAirlineId());
        dto.setPartnerAirlineId(entity.getPartnerAirlineId());
        dto.setAgreementNumber(entity.getAgreementNumber());
        dto.setAgreementType(entity.getAgreementType());
        dto.setAgreementStatus(entity.getAgreementStatus());
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
    public CodesharePartner toEntity(CodesharePartnerDTO dto) {
        if (dto == null) {
            return null;
        }

        CodesharePartner entity = new CodesharePartner();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    @Override
    public void updateEntityFromDto(CodesharePartnerDTO dto, CodesharePartner entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setTenantId(dto.getTenantId());
        entity.setHomeAirlineId(dto.getHomeAirlineId());
        entity.setPartnerAirlineId(dto.getPartnerAirlineId());
        entity.setAgreementNumber(dto.getAgreementNumber());
        entity.setAgreementType(dto.getAgreementType());
        entity.setAgreementStatus(dto.getAgreementStatus());
        entity.setActive(dto.getActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setDescription(dto.getDescription());
        entity.setRemarks(dto.getRemarks());
        entity.setRecordStatus(dto.getRecordStatus());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
    }

    @Override
    public List<CodesharePartnerDTO> toDTOList(List<CodesharePartner> list) {
        return list == null ? List.of() : list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartnerDTO> toDTOSet(Set<CodesharePartner> set) {
        return set == null ? Set.of() : set.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @Override
    public List<CodesharePartner> toEntityList(List<CodesharePartnerDTO> list) {
        return list == null ? List.of() : list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<CodesharePartner> toEntitySet(Set<CodesharePartnerDTO> set) {
        return set == null ? Set.of() : set.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
