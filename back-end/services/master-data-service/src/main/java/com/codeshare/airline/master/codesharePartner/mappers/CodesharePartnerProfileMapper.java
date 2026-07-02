package com.codeshare.airline.master.codesharepartner.mappers;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.codesharepartner.entities.CodesharePartnerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerProfileMapper extends CSMGenericMapper<CodesharePartnerProfile, CodesharePartnerProfileDTO> {
    @Mapping(source = "partner.id", target = "partnerId")
    CodesharePartnerProfileDTO toDTO(CodesharePartnerProfile entity);

    @Mapping(target = "partner", ignore = true)
    CodesharePartnerProfile toEntity(CodesharePartnerProfileDTO dto);

    @Mapping(target = "partner", ignore = true)
    void updateEntityFromDto(CodesharePartnerProfileDTO dto, @MappingTarget CodesharePartnerProfile entity);
}
