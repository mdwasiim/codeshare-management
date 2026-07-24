package com.codeshare.airline.master.airlines.mappers;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AirlineMapper extends CSMGenericMapper<Airline, AirlineCarrierDTO> {
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "headquartersCity.id", target = "headquartersCityId")
    @Mapping(source = "homeAirport.id", target = "homeAirportId")
    @Mapping(source = "alliance.id", target = "allianceId")
    AirlineCarrierDTO toDTO(Airline entity);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "headquartersCity", ignore = true)
    @Mapping(target = "homeAirport", ignore = true)
    @Mapping(target = "alliance", ignore = true)
    Airline toEntity(AirlineCarrierDTO dto);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "headquartersCity", ignore = true)
    @Mapping(target = "homeAirport", ignore = true)
    @Mapping(target = "alliance", ignore = true)
    void updateEntityFromDto(AirlineCarrierDTO dto, @MappingTarget Airline entity);
}
