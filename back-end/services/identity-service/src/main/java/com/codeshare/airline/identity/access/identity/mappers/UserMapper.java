package com.codeshare.airline.identity.access.identity.mappers;

import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface UserMapper extends CSMGenericMapper<User, AuthUserDTO> {

}
