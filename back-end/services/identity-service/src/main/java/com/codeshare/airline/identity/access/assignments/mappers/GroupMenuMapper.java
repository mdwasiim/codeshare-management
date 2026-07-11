package com.codeshare.airline.identity.access.assignments.mappers;


import com.codeshare.airline.platform.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface GroupMenuMapper extends CSMGenericMapper<GroupMenu, GroupMenuDTO> {
    @Override
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "menuId", source = "menu.id")
    GroupMenuDTO toDTO(GroupMenu entity);
}
