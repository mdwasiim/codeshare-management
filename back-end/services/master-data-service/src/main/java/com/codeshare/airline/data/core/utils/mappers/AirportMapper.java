package com.codeshare.airline.data.core.utils.mappers;

import com.codeshare.airline.core.dto.georegion.AirportDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.core.eitities.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(
        config = CSMMapperConfig.class,
        uses = {
                CityMapper.class,
                TimezoneMapper.class,
                DstRuleMapper.class
        }
)
public interface AirportMapper
        extends CSMGenericMapper<Airport, AirportDTO> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "timezone.id", target = "timezoneId")
    AirportDTO toDTO(Airport entity);
}