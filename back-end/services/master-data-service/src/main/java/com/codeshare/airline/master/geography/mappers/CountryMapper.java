package com.codeshare.airline.master.geography.mappers;

import com.codeshare.airline.platform.core.dto.master.georegion.CountryDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.geography.entities.Country;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class,
        uses = {StateMapper.class, RegionMapper.class}
)
public interface CountryMapper extends CSMGenericMapper<Country, CountryDTO> {

}