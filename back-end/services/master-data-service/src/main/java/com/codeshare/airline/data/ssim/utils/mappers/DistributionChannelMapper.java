package com.codeshare.airline.data.ssim.utils.mappers;

import com.codeshare.airline.core.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.ssim.eitities.DistributionChannel;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DistributionChannelMapper
        extends CSMGenericMapper<DistributionChannel, DistributionChannelDTO> {
}
