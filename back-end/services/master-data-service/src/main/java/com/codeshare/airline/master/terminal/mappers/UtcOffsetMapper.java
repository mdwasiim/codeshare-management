package com.codeshare.airline.master.terminal.mappers;

import com.codeshare.airline.platform.core.dto.master.terminal.UtcOffsetDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.terminal.entities.UtcOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface UtcOffsetMapper extends CSMGenericMapper<UtcOffset, UtcOffsetDTO> {

    @Mapping(source = "timezone.id", target = "timezoneId")
    @Mapping(source = "timezone.tzIdentifier", target = "timezoneIdentifier")
    UtcOffsetDTO toDTO(UtcOffset entity);

    @Mapping(target = "timezone", ignore = true)
    UtcOffset toEntity(UtcOffsetDTO dto);

    @Mapping(target = "timezone", ignore = true)
    void updateEntityFromDto(UtcOffsetDTO dto, @MappingTarget UtcOffset entity);
}
