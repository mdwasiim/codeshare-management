package com.codeshare.airline.master.geography.mappers;

import com.codeshare.airline.platform.core.dto.master.georegion.RegionDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.geography.entities.Region;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface RegionMapper extends CSMGenericMapper<Region, RegionDTO> {


}