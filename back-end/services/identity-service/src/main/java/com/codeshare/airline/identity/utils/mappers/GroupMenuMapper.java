package com.codeshare.airline.utils.mappers;


import com.codeshare.airline.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.identity.entities.GroupMenu;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface GroupMenuMapper extends CSMGenericMapper<GroupMenu, GroupMenuDTO> {

}
