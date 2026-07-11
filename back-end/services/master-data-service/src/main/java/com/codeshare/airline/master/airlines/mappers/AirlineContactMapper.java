package com.codeshare.airline.master.airlines.mappers;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineContactDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airlines.entities.AirlineContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AirlineContactMapper extends CSMGenericMapper<AirlineContact, AirlineContactDTO> {
    @Mapping(source = "airline.id", target = "airlineId")
    @Mapping(source = "timeZone.id", target = "timeZoneId")
    AirlineContactDTO toDTO(AirlineContact entity);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "timeZone", ignore = true)
    AirlineContact toEntity(AirlineContactDTO dto);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "timeZone", ignore = true)
    void updateEntityFromDto(AirlineContactDTO dto, @MappingTarget AirlineContact entity);
}
