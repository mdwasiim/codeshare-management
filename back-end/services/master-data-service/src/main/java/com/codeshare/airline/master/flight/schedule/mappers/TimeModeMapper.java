package com.codeshare.airline.master.flight.schedule.mappers;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TimeModeDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.schedule.entities.TimeMode;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TimeModeMapper extends CSMGenericMapper<TimeMode, TimeModeDTO> {
}
