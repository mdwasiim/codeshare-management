package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.services.mapper.GenericMapper;
import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import com.codeshare.airline.tenant.entities.TenantOrganization;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = GenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantOrganizationMapper extends GenericMapper<TenantOrganization, TenantOrganizationDTO> {

}
