package com.codeshare.airline.master.flightcommercial.passenger.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ServiceTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ServiceType;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ServiceTypeMapper extends CSMGenericMapper<ServiceType, ServiceTypeDTO> {
}
