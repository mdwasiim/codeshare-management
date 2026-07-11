package com.codeshare.airline.identity.access.authentication.mappers;

import com.codeshare.airline.platform.core.dto.auth.UserDeviceDTO;
import com.codeshare.airline.identity.access.authentication.entities.UserDeviceEntity;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface AuthUserDeviceMapper extends CSMGenericMapper<UserDeviceEntity, UserDeviceDTO> {
}
