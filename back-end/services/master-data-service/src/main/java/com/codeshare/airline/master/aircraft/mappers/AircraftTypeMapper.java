package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.aircraft.AircraftTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.eitities.AircraftType;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AircraftTypeMapper
        extends CSMGenericMapper<AircraftType, AircraftTypeDTO> {
}