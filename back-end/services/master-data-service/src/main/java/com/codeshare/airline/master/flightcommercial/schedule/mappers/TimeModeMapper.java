package com.codeshare.airline.master.flightcommercial.schedule.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TimeModeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TimeMode;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TimeModeMapper extends CSMGenericMapper<TimeMode, TimeModeDTO> {
}
