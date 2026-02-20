package com.codeshare.airline.core.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

public interface CSMGenericMapper<E , D > {

    D toDTO(E entity);

    E toEntity(D dto);

    // ===== UPDATE =====
    void updateEntityFromDto(D dto, @MappingTarget E entity);

    List<D> toDTOList(List<E> list);
    Set<D> toDTOSet(Set<E> set);

    List<E> toEntityList(List<D> list);
    Set<E> toEntitySet(Set<D> set);
}
