package com.codeshare.airline.identity.access.identity.mappers;

import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface AuthUserMapper extends CSMGenericMapper<User, UserDetailsAdapter> {

}
