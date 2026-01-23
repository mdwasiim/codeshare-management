package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.auth.model.entities.GroupMenu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMenuMapper extends CSMGenericMapper<GroupMenu, GroupMenuDTO> {

}
