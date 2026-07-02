package com.codeshare.airline.master.flightcommercial.schedule.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.FlightFrequencyDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flightcommercial.schedule.entities.FlightFrequency;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface FlightFrequencyMapper extends CSMGenericMapper<FlightFrequency, FlightFrequencyDTO> {
}
