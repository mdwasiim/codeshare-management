package com.codeshare.airline.utils.mappers;

import com.codeshare.airline.identity.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.entities.User;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface AuthUserMapper extends CSMGenericMapper<User, UserDetailsAdapter> {

}
