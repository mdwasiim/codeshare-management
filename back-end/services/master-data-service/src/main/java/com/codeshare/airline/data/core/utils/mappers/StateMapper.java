package com.codeshare.airline.data.core.utils.mappers;

import com.codeshare.airline.core.dto.georegion.StateDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.core.eitities.State;
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