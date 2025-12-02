package com.codeshare.airline.common.utils.mapper;

import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import com.codeshare.airline.common.utils.mapper.audit.AuditInfo;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

public interface GenericMapper<E extends AbstractEntity, D extends AuditBaseDto> {

    // ========================================================
    // INTERNAL MAPSTRUCT METHODS — always used by MapStruct
    // ========================================================
    @Named("toDTOInternal")
    @Mapping(target = "audit", ignore = true)
    D toDTO(E entity, @Context AuditMapper auditMapper);

    @Named("toDTOListInternal")
    @IterableMapping(qualifiedByName = "toDTOInternal")
    List<D> toDTOList(List<E> list, @Context AuditMapper auditMapper);

    @Named("toDTOSetInternal")
    @IterableMapping(qualifiedByName = "toDTOInternal")
    Set<D> toDTOSet(Set<E> set, @Context AuditMapper auditMapper);


    // ========================================================
    // PUBLIC WRAPPERS (MapStruct IGNORE because names differ)
    // ========================================================
    default D map(E entity) {
        return toDTO(entity, AuditMapperHolder.get());
    }

    default List<D> mapList(List<E> list) {
        return toDTOList(list, AuditMapperHolder.get());
    }

    default Set<D> mapSet(Set<E> set) {
        return toDTOSet(set, AuditMapperHolder.get());
    }


    // ========================================================
    // DTO → ENTITY
    // ========================================================
    @Named("toEntityDefault")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    E toEntity(D dto);


    // ========================================================
    // AUDIT POPULATION
    // ========================================================
    @AfterMapping
    default void addAuditFields(E entity, @MappingTarget D dto, @Context AuditMapper auditMapper) {
        if (entity == null || dto == null) return;
        AuditInfo audit = auditMapper.toAuditInfo(entity);
        dto.setAudit(audit);
    }
}



