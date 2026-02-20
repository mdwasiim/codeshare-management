package com.codeshare.airline.persistence.persistence.pagination.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSMDataPageRequestDTO {

    private int page = 0;      // default
    private int size = 10;     // default
    private String sortBy = "id";
    private String direction = "asc";
}
