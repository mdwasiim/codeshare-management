package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.audit.AuditMapper;
import com.codeshare.airline.common.tenant.model.OrganizationDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.tenant.entities.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface OrganizationMapper extends GenericMapper<Organization, OrganizationDTO> {
    Organization toEntityFromRequest(OrganizationDTO dto);
}
