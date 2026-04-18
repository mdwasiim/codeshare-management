package com.codeshare.airline.master.airport.georegion.utils.mappers;

import com.codeshare.airline.dto.airport.georegion.DstRuleDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.DstRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface DstRuleMapper
        extends CSMGenericMapper<DstRule, DstRuleDTO> {

    @Mapping(source = "timezone.id", target = "timezoneId")
    DstRuleDTO toDTO(DstRule entity);
}