package com.codeshare.airline.master.terminal.mappers;

import com.codeshare.airline.core.dto.master.terminal.DaylightSavingRuleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.terminal.entities.DaylightSavingRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface DaylightSavingRuleMapper extends CSMGenericMapper<DaylightSavingRule, DaylightSavingRuleDTO> {

    @Mapping(source = "timezone.id", target = "timezoneId")
    @Mapping(source = "timezone.tzIdentifier", target = "timezoneIdentifier")
    DaylightSavingRuleDTO toDTO(DaylightSavingRule entity);

    @Mapping(target = "timezone", ignore = true)
    DaylightSavingRule toEntity(DaylightSavingRuleDTO dto);

    @Mapping(target = "timezone", ignore = true)
    void updateEntityFromDto(DaylightSavingRuleDTO dto, @MappingTarget DaylightSavingRule entity);
}
