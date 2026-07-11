package com.codeshare.airline.master.flight.schedule.mappers;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionCode;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TrafficRestrictionCodeMapper extends CSMGenericMapper<TrafficRestrictionCode, TrafficRestrictionCodeDTO> {
}
