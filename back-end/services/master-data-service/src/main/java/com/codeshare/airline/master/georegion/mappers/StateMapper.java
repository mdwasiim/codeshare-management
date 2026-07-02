package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.master.georegion.StateDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.entities.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface StateMapper
        extends CSMGenericMapper<State, StateDTO> {

    @Mapping(source = "stateCode", target = "code")
    @Mapping(source = "stateName", target = "name")
    @Mapping(source = "country.id", target = "countryId")
    StateDTO toDTO(State entity);

    @Mapping(source = "code", target = "stateCode")
    @Mapping(source = "name", target = "stateName")
    @Mapping(target = "country", ignore = true)
    State toEntity(StateDTO dto);

    @Mapping(source = "code", target = "stateCode")
    @Mapping(source = "name", target = "stateName")
    @Mapping(target = "country", ignore = true)
    void updateEntityFromDto(StateDTO dto, @MappingTarget State entity);
}
