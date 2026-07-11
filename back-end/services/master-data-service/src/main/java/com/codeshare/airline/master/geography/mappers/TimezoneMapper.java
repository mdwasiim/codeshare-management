package com.codeshare.airline.master.geography.mappers;

import com.codeshare.airline.platform.core.dto.master.georegion.TimezoneDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.geography.entities.Timezone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface TimezoneMapper
        extends CSMGenericMapper<Timezone, TimezoneDTO> {

    @Mapping(source = "tzIdentifier", target = "zoneId")
    @Mapping(target = "utcOffset", expression = "java(formatUtcOffset(entity.getStandardUtcOffsetMinutes()))")
    @Mapping(source = "observesDst", target = "isDstSupported")
    TimezoneDTO toDTO(Timezone entity);

    @Mapping(source = "zoneId", target = "tzIdentifier")
    @Mapping(target = "observesDst", ignore = true)
    @Mapping(target = "standardUtcOffsetMinutes", ignore = true)
    Timezone toEntity(TimezoneDTO dto);

    @Mapping(source = "zoneId", target = "tzIdentifier")
    @Mapping(target = "observesDst", ignore = true)
    @Mapping(target = "standardUtcOffsetMinutes", ignore = true)
    void updateEntityFromDto(TimezoneDTO dto, @MappingTarget Timezone entity);

    default String formatUtcOffset(Integer offsetMinutes) {
        if (offsetMinutes == null) {
            return null;
        }

        int absoluteMinutes = Math.abs(offsetMinutes);
        int hours = absoluteMinutes / 60;
        int minutes = absoluteMinutes % 60;
        String sign = offsetMinutes < 0 ? "-" : "+";

        return String.format("%s%02d:%02d", sign, hours, minutes);
    }
}
