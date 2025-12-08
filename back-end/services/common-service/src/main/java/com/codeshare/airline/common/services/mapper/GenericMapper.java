package com.codeshare.airline.common.services.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@MapperConfig(componentModel = "spring")
public interface GenericMapper<E , D > {

    D toDTO(E entity);

    E toEntity(D dto);


    // 🔥 NEW: Common UPDATE method for all mappers
    void updateEntityFromDto(D dto, @MappingTarget E entity);

    List<D> toDTOList(List<E> list);
    Set<D> toDTOSet(Set<E> set);

    List<E> toEntityList(List<D> list);
    Set<E> toEntitySet(Set<D> set);
}
