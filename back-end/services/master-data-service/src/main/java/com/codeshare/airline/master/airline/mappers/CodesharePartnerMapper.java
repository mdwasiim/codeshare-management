package com.codeshare.airline.master.airline.mappers;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airline.entities.CodesharePartner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CodesharePartnerMapper extends CSMGenericMapper<CodesharePartner, CodesharePartnerDTO> {
    @Mapping(source = "homeAirline.id", target = "homeAirlineId")
    @Mapping(source = "partnerAirline.id", target = "partnerAirlineId")
    CodesharePartnerDTO toDTO(CodesharePartner entity);

    @Mapping(target = "homeAirline", ignore = true)
    @Mapping(target = "partnerAirline", ignore = true)
    CodesharePartner toEntity(CodesharePartnerDTO dto);

    @Mapping(target = "homeAirline", ignore = true)
    @Mapping(target = "partnerAirline", ignore = true)
    void updateEntityFromDto(CodesharePartnerDTO dto, @MappingTarget CodesharePartner entity);
}
