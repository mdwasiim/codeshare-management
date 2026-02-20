package com.codeshare.airline.data.ssim.utils.mappers;

import com.codeshare.airline.core.dto.ssim.DeiDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.ssim.eitities.Dei;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DeiMapper
        extends CSMGenericMapper<Dei, DeiDTO> {
}