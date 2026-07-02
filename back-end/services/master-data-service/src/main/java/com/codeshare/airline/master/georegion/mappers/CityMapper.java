package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.CityDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.eitities.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        config = CSMMapperConfig.class
)
public interface CityMapper
        extends CSMGenericMapper<City, CityDTO> {

    @Mapping(source = "cityName", target = "name")
    @Mapping(source = "iataCityCode", target = "iataCode")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "state.id", target = "stateId")
    CityDTO toDTO(City entity);

    @Mapping(source = "name", target = "cityName")
    @Mapping(source = "iataCode", target = "iataCityCode")
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    City toEntity(CityDTO dto);

    @Mapping(source = "name", target = "cityName")
    @Mapping(source = "iataCode", target = "iataCityCode")
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    void updateEntityFromDto(CityDTO dto, @MappingTarget City entity);
}
