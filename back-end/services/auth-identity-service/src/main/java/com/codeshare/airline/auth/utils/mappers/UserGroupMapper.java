package com.codeshare.airline.auth.utils.mappers;


import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.entities.rbac.UserGroup;
import com.codeshare.airline.common.auth.identity.model.UserGroupDTO;
import com.codeshare.airline.common.services.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        config = GenericMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserGroupMapper extends GenericMapper<UserGroup, UserGroupDTO> {

    // ENTITY → DTO
    @Override
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "group.id", target = "groupId")
    UserGroupDTO toDTO(UserGroup entity);

    // DTO → ENTITY
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(toUser(dto.getUserId()))")
    @Mapping(target = "group", expression = "java(toGroup(dto.getGroupId()))")
    UserGroup toEntity(UserGroupDTO dto);

    // UPDATE
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(toUser(dto.getUserId()))")
    @Mapping(target = "group", expression = "java(toGroup(dto.getGroupId()))")
    void updateEntityFromDto(UserGroupDTO dto, @MappingTarget UserGroup entity);

    // Helpers
    default User toUser(UUID id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    default Group toGroup(UUID id) {
        if (id == null) return null;
        Group g = new Group();
        g.setId(id);
        return g;
    }
}

