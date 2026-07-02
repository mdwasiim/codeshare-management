package com.codeshare.airline.master.terminal.mappers;

import com.codeshare.airline.core.dto.master.terminal.PassengerTerminalDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.terminal.entities.PassengerTerminal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface PassengerTerminalMapper
        extends CSMGenericMapper<PassengerTerminal, PassengerTerminalDTO> {

    @Mapping(source = "airport.iataCode", target = "airportCode")
    PassengerTerminalDTO toDTO(PassengerTerminal entity);

    @Mapping(target = "airport", ignore = true)
    PassengerTerminal toEntity(PassengerTerminalDTO dto);

    @Mapping(target = "airport", ignore = true)
    void updateEntityFromDto(PassengerTerminalDTO dto, @MappingTarget PassengerTerminal entity);
}
