package com.codeshare.airline.identity.utils.mappers;

import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface UserMapper extends CSMGenericMapper<User, AuthUserDTO> {

}
