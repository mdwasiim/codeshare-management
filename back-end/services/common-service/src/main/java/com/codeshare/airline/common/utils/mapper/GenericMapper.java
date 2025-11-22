package com.codeshare.airline.common.utils.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

public interface GenericMapper<E, D> {

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
}
