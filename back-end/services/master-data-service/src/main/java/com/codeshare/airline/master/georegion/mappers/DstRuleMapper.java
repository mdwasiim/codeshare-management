package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.DstRuleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.eitities.DstRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface DstRuleMapper
        extends CSMGenericMapper<DstRule, DstRuleDTO> {

    @Mapping(source = "timezone.id", target = "timezoneId")
    DstRuleDTO toDTO(DstRule entity);
}