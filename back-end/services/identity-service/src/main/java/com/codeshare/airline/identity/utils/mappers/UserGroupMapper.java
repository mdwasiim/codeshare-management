package com.codeshare.airline.identity.utils.mappers;

import com.codeshare.airline.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.identity.entities.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface UserGroupMapper extends CSMGenericMapper<UserGroup, UserGroupDTO> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "groupId", source = "group.id")
    UserGroupDTO toDTO(UserGroup entity);
}
