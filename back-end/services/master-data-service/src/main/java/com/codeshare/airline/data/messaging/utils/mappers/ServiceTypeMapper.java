package com.codeshare.airline.data.messaging.utils.mappers;


import com.codeshare.airline.core.dto.airport.georegion.ServiceTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.messaging.eitities.ServiceType;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface ServiceTypeMapper extends CSMGenericMapper<ServiceType, ServiceTypeDTO> {

}