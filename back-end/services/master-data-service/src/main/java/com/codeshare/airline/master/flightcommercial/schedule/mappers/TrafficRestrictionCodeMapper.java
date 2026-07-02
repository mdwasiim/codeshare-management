package com.codeshare.airline.master.flightcommercial.schedule.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TrafficRestrictionCode;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TrafficRestrictionCodeMapper extends CSMGenericMapper<TrafficRestrictionCode, TrafficRestrictionCodeDTO> {
}
