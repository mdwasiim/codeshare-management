package com.codeshare.airline.common.response.pagination;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ServicePageResponseMapper {

    public <T> ServicePageResponse<T> toResponse(Page<T> page) {
        return ServicePageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
