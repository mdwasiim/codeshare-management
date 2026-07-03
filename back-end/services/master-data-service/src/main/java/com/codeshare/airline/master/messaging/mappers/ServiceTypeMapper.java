package com.codeshare.airline.master.messaging.mappers;


import com.codeshare.airline.core.dto.master.messaging.ServiceTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.passenger.entities.ServiceType;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface ServiceTypeMapper extends CSMGenericMapper<ServiceType, ServiceTypeDTO> {

}