package com.codeshare.airline.data.core.utils.mappers;

import com.codeshare.airline.core.dto.georegion.CityDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.core.eitities.City;
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