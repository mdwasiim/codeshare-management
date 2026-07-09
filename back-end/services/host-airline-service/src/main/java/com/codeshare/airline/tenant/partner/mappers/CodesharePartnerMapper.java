package com.codeshare.airline.tenant.partner.mappers;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.partner.entities.CodesharePartner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerMapper extends CSMGenericMapper<CodesharePartner, CodesharePartnerDTO> {

    @Override
    @Mapping(target = "homeAirlineCode", ignore = true)
    @Mapping(target = "homeAirlineName", ignore = true)
    @Mapping(target = "partnerAirlineCode", ignore = true)
    @Mapping(target = "partnerAirlineName", ignore = true)
    CodesharePartnerDTO toDTO(CodesharePartner entity);
}
