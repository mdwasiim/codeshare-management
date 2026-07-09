package com.codeshare.airline.tenant.partner.mappers;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.partner.entities.CodesharePartnerDistributionProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerDistributionProfileMapper extends CSMGenericMapper<CodesharePartnerDistributionProfile, CodesharePartnerDistributionProfileDTO> {

    @Override
    @Mapping(source = "partner.id", target = "partnerId")
    CodesharePartnerDistributionProfileDTO toDTO(CodesharePartnerDistributionProfile entity);

    @Override
    @Mapping(target = "partner", ignore = true)
    CodesharePartnerDistributionProfile toEntity(CodesharePartnerDistributionProfileDTO dto);
}
