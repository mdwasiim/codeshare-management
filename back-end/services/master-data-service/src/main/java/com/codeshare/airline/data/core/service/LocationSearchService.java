package com.codeshare.airline.data.core.service;

import com.codeshare.airline.core.dto.georegion.LocationSearchDTO;

import java.util.List;

public interface LocationSearchService {

    List<LocationSearchDTO> search(String keyword);
}