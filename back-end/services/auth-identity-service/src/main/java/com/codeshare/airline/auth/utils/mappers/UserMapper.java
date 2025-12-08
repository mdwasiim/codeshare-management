package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.auth.identity.model.UserDTO;
import com.codeshare.airline.common.services.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = GenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends GenericMapper<User, UserDTO> {

    @Override
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "userGroupRoles", ignore = true)
    User toEntity(UserDTO dto);
}
