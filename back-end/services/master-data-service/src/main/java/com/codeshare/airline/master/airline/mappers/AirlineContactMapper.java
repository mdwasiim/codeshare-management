package com.codeshare.airline.master.airline.mappers;

import com.codeshare.airline.core.dto.master.airline.AirlineContactDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airline.entities.AirlineContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AirlineContactMapper extends CSMGenericMapper<AirlineContact, AirlineContactDTO> {
    @Mapping(source = "airline.id", target = "airlineId")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "timeZone.id", target = "timeZoneId")
    AirlineContactDTO toDTO(AirlineContact entity);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "timeZone", ignore = true)
    AirlineContact toEntity(AirlineContactDTO dto);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "timeZone", ignore = true)
    void updateEntityFromDto(AirlineContactDTO dto, @MappingTarget AirlineContact entity);
}
