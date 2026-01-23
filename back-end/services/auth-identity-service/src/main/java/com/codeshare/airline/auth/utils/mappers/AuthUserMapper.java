package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.auth.model.entities.User;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthUserMapper extends CSMGenericMapper<User, UserDetailsAdapter> {

}
