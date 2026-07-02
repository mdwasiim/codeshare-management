package com.codeshare.airline.master.georegion.service;

import com.codeshare.airline.core.dto.master.georegion.LocationSearchDTO;

import java.util.List;

public interface LocationSearchService {

    List<LocationSearchDTO> search(String keyword);
}