package com.codeshare.airline.master.airport.terminal.mappers;

import com.codeshare.airline.core.dto.airport.terminal.PassengerTerminalDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.terminal.eitities.PassengerTerminal;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface PassengerTerminalMapper
        extends CSMGenericMapper<PassengerTerminal, PassengerTerminalDTO> {

}