package com.codeshare.airline.master.airport.georegion.utils.mappers;

import com.codeshare.airline.dto.airport.georegion.CountryDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.Country;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class,
        uses = {StateMapper.class, RegionMapper.class}
)
public interface CountryMapper extends CSMGenericMapper<Country, CountryDTO> {

}