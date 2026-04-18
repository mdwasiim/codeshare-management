package com.codeshare.airline.master.airport.terminal.utils.mappers;

import com.codeshare.airline.dto.airport.terminal.PassengerTerminalDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.terminal.eitities.PassengerTerminal;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface PassengerTerminalMapper
        extends CSMGenericMapper<PassengerTerminal, PassengerTerminalDTO> {

}