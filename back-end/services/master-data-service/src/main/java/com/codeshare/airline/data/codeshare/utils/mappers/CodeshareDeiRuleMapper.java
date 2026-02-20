package com.codeshare.airline.data.codeshare.utils.mappers;

import com.codeshare.airline.core.dto.codeshare.CodeshareDeiRuleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.codeshare.eitities.CodeshareDeiRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareDeiRuleMapper
        extends CSMGenericMapper<CodeshareDeiRule, CodeshareDeiRuleDTO> {

    @Mapping(source = "flightMapping.id", target = "flightMappingId")
    @Mapping(source = "dei.id", target = "deiId")
    CodeshareDeiRuleDTO toDTO(CodeshareDeiRule entity);
}