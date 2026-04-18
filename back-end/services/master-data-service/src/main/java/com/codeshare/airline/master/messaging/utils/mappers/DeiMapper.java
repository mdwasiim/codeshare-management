package com.codeshare.airline.messaging.utils.mappers;

import com.codeshare.airline.dto.ssim.DeiDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.messaging.eitities.DeiRegistry;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DeiMapper
        extends CSMGenericMapper<DeiRegistry, DeiDTO> {
}