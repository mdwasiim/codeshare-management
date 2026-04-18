package com.codeshare.airline.utils.mappers;

import com.codeshare.airline.entities.UserDeviceEntity;
import com.codeshare.airline.core.dto.auth.AuthUserDeviceDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface AuthUserDeviceMapper extends CSMGenericMapper<UserDeviceEntity, AuthUserDeviceDTO> {
}
