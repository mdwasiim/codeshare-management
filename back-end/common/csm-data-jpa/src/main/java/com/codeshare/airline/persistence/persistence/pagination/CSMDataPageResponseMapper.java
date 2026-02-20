package com.codeshare.airline.persistence.persistence.pagination;


import com.codeshare.airline.persistence.persistence.pagination.dto.CSMDataPageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CSMDataPageResponseMapper {

    public <T> CSMDataPageResponseDTO<T> toResponse(Page<T> page) {
        return CSMDataPageResponseDTO.<T>builder()
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
