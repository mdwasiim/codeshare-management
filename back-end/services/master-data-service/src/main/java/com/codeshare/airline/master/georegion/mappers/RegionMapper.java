package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.RegionDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.eitities.Region;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface RegionMapper extends CSMGenericMapper<Region, RegionDTO> {


}