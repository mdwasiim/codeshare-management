package com.codeshare.airline.persistence.pagination;


import com.codeshare.airline.persistence.pagination.dto.CSMDataPageRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class CSMDataPageRequestBuilder {

    public static Pageable build(CSMDataPageRequestDTO dto) {
        Sort sort = dto.getDirection().equalsIgnoreCase("desc")
                ? Sort.by(dto.getSortBy()).descending()
                : Sort.by(dto.getSortBy()).ascending();

        return PageRequest.of(dto.getPage(), dto.getSize(), sort);
    }
}
