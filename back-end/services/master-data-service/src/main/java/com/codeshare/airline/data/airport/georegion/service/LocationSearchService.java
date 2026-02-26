package com.codeshare.airline.data.airport.georegion.service;

import com.codeshare.airline.core.dto.airport.georegion.LocationSearchDTO;

import java.util.List;

public interface LocationSearchService {

    List<LocationSearchDTO> search(String keyword);
}