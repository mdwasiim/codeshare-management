package com.codeshare.airline.common.utils.mapper.audit;


import com.codeshare.airline.common.jpa.AbstractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "transactionId", source = "transactionId")
    AuditInfo toAuditInfo(AbstractEntity entity);

}
