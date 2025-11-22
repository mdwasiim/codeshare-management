package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.common.auth.model.UserRoleDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleMapper extends GenericMapper<UserRole, UserRoleDTO> {


}
