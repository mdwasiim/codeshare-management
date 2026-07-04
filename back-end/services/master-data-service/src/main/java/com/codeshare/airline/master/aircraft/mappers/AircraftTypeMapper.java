package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.master.aircraft.AircraftTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AircraftType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AircraftTypeMapper
        extends CSMGenericMapper<AircraftType, AircraftTypeDTO> {

    @Mapping(source = "aircraftFamily.manufacturer.manufacturerName", target = "manufacturer")
    @Mapping(source = "model", target = "modelCode")
    AircraftTypeDTO toDTO(AircraftType entity);
}
