package com.codeshare.airline.master.flight.passenger.mappers;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ServiceTypeDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.passenger.entities.ServiceType;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ServiceTypeMapper extends CSMGenericMapper<ServiceType, ServiceTypeDTO> {
}
