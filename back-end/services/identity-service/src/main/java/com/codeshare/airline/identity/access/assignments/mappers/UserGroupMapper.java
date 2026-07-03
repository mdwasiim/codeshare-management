package com.codeshare.airline.identity.access.assignments.mappers;

import com.codeshare.airline.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface UserGroupMapper extends CSMGenericMapper<UserGroup, UserGroupDTO> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "groupId", source = "group.id")
    UserGroupDTO toDTO(UserGroup entity);
}
