package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.tenant.entities.Tenant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TenantMapper extends GenericMapper<Tenant, TenantDTO> {
    Tenant toEntityFromRequest(TenantDTO dto);
}
