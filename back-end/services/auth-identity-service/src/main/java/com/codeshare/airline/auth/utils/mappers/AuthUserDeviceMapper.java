package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.UserDeviceEntity;
import com.codeshare.airline.core.dto.auth.AuthUserDeviceDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthUserDeviceMapper extends CSMGenericMapper<UserDeviceEntity, AuthUserDeviceDTO> {
}
