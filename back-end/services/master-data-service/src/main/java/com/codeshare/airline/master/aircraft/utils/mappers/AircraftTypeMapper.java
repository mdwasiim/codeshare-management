package com.codeshare.airline.master.aircraft.utils.mappers;

import com.codeshare.airline.dto.aircraft.AircraftTypeDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.eitities.AircraftType;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AircraftTypeMapper
        extends CSMGenericMapper<AircraftType, AircraftTypeDTO> {
}