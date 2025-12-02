package com.codeshare.airline.common.utils.mapper.audit;

import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    // Automatically maps:
    // createdAt → createdAt
    // updatedAt → updatedAt
    // createdBy → createdBy
    // updatedBy → updatedBy
    // transactionId → transactionId
    // deleted → deleted
    // deletedAt → deletedAt
    // deletedBy → deletedBy
    AuditInfo toAuditInfo(AbstractEntity entity);

}
