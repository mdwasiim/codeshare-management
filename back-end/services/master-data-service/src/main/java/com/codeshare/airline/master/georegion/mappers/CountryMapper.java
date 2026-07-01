package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.CountryDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.eitities.Country;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class,
        uses = {StateMapper.class, RegionMapper.class}
)
public interface CountryMapper extends CSMGenericMapper<Country, CountryDTO> {

}