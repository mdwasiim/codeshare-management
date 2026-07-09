package com.codeshare.airline.tenant.core.mappers;

import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.core.entities.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface TenantMapper extends CSMGenericMapper<Tenant, TenantDTO> {

    @Override
    @Mapping(source = "tenantCode", target = "code")
    TenantDTO toDTO(Tenant entity);

    @Override
    @Mapping(source = "code", target = "tenantCode")
    Tenant toEntity(TenantDTO dto);
}
