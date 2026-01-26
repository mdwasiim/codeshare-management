package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.auth.entities.Tenant;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        config = CSMGenericMapper.class
)
public interface TenantMapper extends CSMGenericMapper<Tenant, TenantDTO> {

}

