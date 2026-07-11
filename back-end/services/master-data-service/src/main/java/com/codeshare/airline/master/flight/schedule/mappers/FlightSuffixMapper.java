package com.codeshare.airline.master.flight.schedule.mappers;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.FlightSuffixDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.schedule.entities.FlightSuffix;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface FlightSuffixMapper extends CSMGenericMapper<FlightSuffix, FlightSuffixDTO> {
}
