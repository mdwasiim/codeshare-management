package com.codeshare.airline.identity.utils.mappers;

import com.codeshare.airline.core.dto.auth.UserDeviceDTO;
import com.codeshare.airline.identity.entities.UserDeviceEntity;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface AuthUserDeviceMapper extends CSMGenericMapper<UserDeviceEntity, UserDeviceDTO> {
}
