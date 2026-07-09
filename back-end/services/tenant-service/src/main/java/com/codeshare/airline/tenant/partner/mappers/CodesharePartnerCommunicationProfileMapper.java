package com.codeshare.airline.tenant.partner.mappers;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.partner.entities.CodesharePartnerCommunicationProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerCommunicationProfileMapper extends CSMGenericMapper<CodesharePartnerCommunicationProfile, CodesharePartnerCommunicationProfileDTO> {

    @Override
    @Mapping(source = "partner.id", target = "partnerId")
    CodesharePartnerCommunicationProfileDTO toDTO(CodesharePartnerCommunicationProfile entity);

    @Override
    @Mapping(target = "partner", ignore = true)
    CodesharePartnerCommunicationProfile toEntity(CodesharePartnerCommunicationProfileDTO dto);
}
