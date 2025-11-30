package com.codeshare.airline.common.response.pagination;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicePageRequestDTO {

    private int page = 0;      // default
    private int size = 10;     // default
    private String sortBy = "id";
    private String direction = "asc";
}
