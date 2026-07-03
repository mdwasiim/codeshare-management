package com.codeshare.airline.master.airlines.codesharepartner.mappers;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airlines.codesharepartner.entities.CodesharePartnerCommunicationProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerCommunicationProfileMapper extends CSMGenericMapper<CodesharePartnerCommunicationProfile, CodesharePartnerCommunicationProfileDTO> {
    @Mapping(source = "partner.id", target = "partnerId")
    CodesharePartnerCommunicationProfileDTO toDTO(CodesharePartnerCommunicationProfile entity);

    @Mapping(target = "partner", ignore = true)
    CodesharePartnerCommunicationProfile toEntity(CodesharePartnerCommunicationProfileDTO dto);

    @Mapping(target = "partner", ignore = true)
    void updateEntityFromDto(CodesharePartnerCommunicationProfileDTO dto, @MappingTarget CodesharePartnerCommunicationProfile entity);
}
