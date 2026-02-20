package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.GroupMenu;
import com.codeshare.airline.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface GroupMenuMapper extends CSMGenericMapper<GroupMenu, GroupMenuDTO> {

}
