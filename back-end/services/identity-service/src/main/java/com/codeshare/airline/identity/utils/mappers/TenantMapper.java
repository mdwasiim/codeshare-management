package com.codeshare.airline.utils.mappers;

import com.codeshare.airline.entities.Tenant;
import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TenantMapper extends CSMGenericMapper<Tenant, TenantDTO> {

}

