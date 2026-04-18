package com.codeshare.airline.master.airport.georegion.utils.mappers;

import com.codeshare.airline.dto.airport.georegion.StateDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class,
        uses = {CityMapper.class}
)
public interface StateMapper
        extends CSMGenericMapper<State, StateDTO> {

    @Mapping(source = "country.id", target = "countryId")
    StateDTO toDTO(State entity);

    @Mapping(source = "countryId", target = "country.id")
    State toEntity(StateDTO dto);
}