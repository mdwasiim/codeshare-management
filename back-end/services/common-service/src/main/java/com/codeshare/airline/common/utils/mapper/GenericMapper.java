package com.codeshare.airline.common.utils.mapper;

import com.codeshare.airline.common.jpa.AbstractEntity;
import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import com.codeshare.airline.common.utils.mapper.audit.AuditInfo;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

public interface GenericMapper<E extends AbstractEntity, D extends AuditBaseDto>{

    D toDTO(E entity);

    @Named("toEntityDefault")
    E toEntity(D dto);

    // LIST
    @IterableMapping(qualifiedByName = "toEntityDefault")
    List<E> toEntityList(List<D> users);
    List<D> toDTOList(List<E> users);

    // SET
    @IterableMapping(qualifiedByName = "toEntityDefault")
    Set<E> toEntitySet(Set<D> users);
    Set<D> toDTOSet(Set<E> users);

    // 🔥 AUTO-MAP AUDIT FIELDS FOR ALL DTOs
    @AfterMapping
    default void addAuditFields(E entity, @MappingTarget D dto, @Context AuditMapper auditMapper) {
        if (entity != null && dto != null) {
            AuditInfo info = auditMapper.toAuditInfo(entity);
            dto.setAudit(info);
        }
    }
}
