package com.codeshare.airline.master.airport.georegion.utils.mappers;

import com.codeshare.airline.dto.airport.georegion.CityDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class,
        uses = {AirportMapper.class}
)
public interface CityMapper
        extends CSMGenericMapper<City, CityDTO> {

    @Mapping(source = "state.id", target = "stateId")
    CityDTO toDTO(City entity);
}