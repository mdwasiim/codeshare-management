package com.codeshare.airline.data.codeshare.utils.mappers;

import com.codeshare.airline.core.dto.codeshare.CodeshareDayRuleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.codeshare.eitities.CodeshareDayRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareDayRuleMapper
        extends CSMGenericMapper<CodeshareDayRule, CodeshareDayRuleDTO> {

    @Mapping(source = "flightMapping.id", target = "flightMappingId")
    CodeshareDayRuleDTO toDTO(CodeshareDayRule entity);
}