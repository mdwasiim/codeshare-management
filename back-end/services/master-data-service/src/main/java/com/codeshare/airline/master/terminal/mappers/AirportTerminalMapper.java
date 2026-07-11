package com.codeshare.airline.master.terminal.mappers;

import com.codeshare.airline.platform.core.dto.master.terminal.AirportTerminalDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.terminal.entities.AirportTerminal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AirportTerminalMapper extends CSMGenericMapper<AirportTerminal, AirportTerminalDTO> {

    @Mapping(source = "airport.id", target = "airportId")
    @Mapping(source = "airport.iataCode", target = "airportCode")
    AirportTerminalDTO toDTO(AirportTerminal entity);

    @Mapping(target = "airport", ignore = true)
    AirportTerminal toEntity(AirportTerminalDTO dto);

    @Mapping(target = "airport", ignore = true)
    void updateEntityFromDto(AirportTerminalDTO dto, @MappingTarget AirportTerminal entity);
}
