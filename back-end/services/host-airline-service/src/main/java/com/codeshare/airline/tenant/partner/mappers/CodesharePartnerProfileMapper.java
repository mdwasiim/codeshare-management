package com.codeshare.airline.tenant.partner.mappers;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.partner.entities.CodesharePartnerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerProfileMapper extends CSMGenericMapper<CodesharePartnerProfile, CodesharePartnerProfileDTO> {

    @Override
    @Mapping(source = "partner.id", target = "partnerId")
    CodesharePartnerProfileDTO toDTO(CodesharePartnerProfile entity);

    @Override
    @Mapping(target = "partner", ignore = true)
    CodesharePartnerProfile toEntity(CodesharePartnerProfileDTO dto);
}
