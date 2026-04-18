package com.codeshare.airline.master.commercial.codeshare.utils.mappers;

import com.codeshare.airline.dto.codeshare.CodeshareDayRuleDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareDayRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareDayRuleMapper
        extends CSMGenericMapper<CodeshareDayRule, CodeshareDayRuleDTO> {

    @Mapping(source = "flightMapping.id", target = "flightMappingId")
    CodeshareDayRuleDTO toDTO(CodeshareDayRule entity);
}