package com.codeshare.airline.common.response.pagination;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ServicePageRequestBuilder {

    public static Pageable build(ServicePageRequestDTO dto) {
        Sort sort = dto.getDirection().equalsIgnoreCase("desc")
                ? Sort.by(dto.getSortBy()).descending()
                : Sort.by(dto.getSortBy()).ascending();

        return PageRequest.of(dto.getPage(), dto.getSize(), sort);
    }
}
