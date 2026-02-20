package com.codeshare.airline.data.codeshare.utils.mappers;

import com.codeshare.airline.core.dto.codeshare.CodeshareRouteDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.codeshare.eitities.CodeshareRoute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareRouteMapper
        extends CSMGenericMapper<CodeshareRoute, CodeshareRouteDTO> {

    @Mapping(source = "agreement.id", target = "agreementId")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "destination.id", target = "destinationId")
    @Mapping(source = "origin.iataCode", target = "originCode")
    @Mapping(source = "destination.iataCode", target = "destinationCode")
    CodeshareRouteDTO toDTO(CodeshareRoute entity);
}