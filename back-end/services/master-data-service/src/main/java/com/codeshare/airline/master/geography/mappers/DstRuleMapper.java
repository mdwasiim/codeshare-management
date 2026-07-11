package com.codeshare.airline.master.geography.mappers;

import com.codeshare.airline.platform.core.dto.master.georegion.DstRuleDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface DstRuleMapper
        extends CSMGenericMapper<TimezoneDLS, DstRuleDTO> {

    @Mapping(source = "timezone.id", target = "timezoneId")
    @Mapping(source = "timezone.tzIdentifier", target = "timezoneIdentifier")
    DstRuleDTO toDTO(TimezoneDLS entity);
}
