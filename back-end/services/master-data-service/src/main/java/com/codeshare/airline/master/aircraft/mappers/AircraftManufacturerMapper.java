package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.master.aircraft.AircraftManufacturerDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AircraftManufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AircraftManufacturerMapper
        extends CSMGenericMapper<AircraftManufacturer, AircraftManufacturerDTO> {

    @Mapping(source = "country.id", target = "countryId")
    AircraftManufacturerDTO toDTO(AircraftManufacturer entity);

    @Mapping(target = "country", ignore = true)
    AircraftManufacturer toEntity(AircraftManufacturerDTO dto);

    @Mapping(target = "country", ignore = true)
    void updateEntityFromDto(AircraftManufacturerDTO dto, @MappingTarget AircraftManufacturer entity);
}
