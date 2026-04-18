package com.codeshare.airline.messaging.utils.mappers;


import com.codeshare.airline.dto.airport.georegion.ServiceTypeDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.messaging.eitities.ServiceType;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface ServiceTypeMapper extends CSMGenericMapper<ServiceType, ServiceTypeDTO> {

}