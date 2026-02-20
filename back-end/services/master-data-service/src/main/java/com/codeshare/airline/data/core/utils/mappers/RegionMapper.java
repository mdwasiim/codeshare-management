package com.codeshare.airline.data.core.utils.mappers;

import com.codeshare.airline.core.dto.georegion.RegionDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.core.eitities.Region;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface RegionMapper extends CSMGenericMapper<Region, RegionDTO> {


}