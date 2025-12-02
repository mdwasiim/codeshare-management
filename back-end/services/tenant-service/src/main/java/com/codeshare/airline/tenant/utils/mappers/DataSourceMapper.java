package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.tenant.model.TenantDataSourceDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import com.codeshare.airline.tenant.entities.DataSource;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { AuditMapper.class })
public interface DataSourceMapper extends GenericMapper<DataSource, TenantDataSourceDTO> {


}
