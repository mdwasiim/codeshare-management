package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.auth.model.UserDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface UserMapper extends GenericMapper<User, UserDTO> {

    @Override
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "userGroupRoles", ignore = true)
    User toEntity(UserDTO dto);
}
