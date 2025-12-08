package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.services.mapper.GenericMapper;
import com.codeshare.airline.common.tenant.model.TenantDataSourceDTO;
import com.codeshare.airline.tenant.entities.TenantDataSource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = GenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantDataSourceMapper extends GenericMapper<TenantDataSource, TenantDataSourceDTO> {


}
