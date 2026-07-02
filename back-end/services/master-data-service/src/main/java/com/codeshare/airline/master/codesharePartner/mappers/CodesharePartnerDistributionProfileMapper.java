package com.codeshare.airline.master.codesharepartner.mappers;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.codesharepartner.entities.CodesharePartnerDistributionProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerDistributionProfileMapper extends CSMGenericMapper<CodesharePartnerDistributionProfile, CodesharePartnerDistributionProfileDTO> {
    @Mapping(source = "partner.id", target = "partnerId")
    CodesharePartnerDistributionProfileDTO toDTO(CodesharePartnerDistributionProfile entity);

    @Mapping(target = "partner", ignore = true)
    CodesharePartnerDistributionProfile toEntity(CodesharePartnerDistributionProfileDTO dto);

    @Mapping(target = "partner", ignore = true)
    void updateEntityFromDto(CodesharePartnerDistributionProfileDTO dto, @MappingTarget CodesharePartnerDistributionProfile entity);
}
