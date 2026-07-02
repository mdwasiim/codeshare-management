package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.master.georegion.DstRuleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.entities.TimezoneDLS;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface DstRuleMapper
        extends CSMGenericMapper<TimezoneDLS, DstRuleDTO> {

    @Mapping(source = "timezone.id", target = "timezoneId")
    DstRuleDTO toDTO(TimezoneDLS entity);
}