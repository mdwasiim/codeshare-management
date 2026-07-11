package com.codeshare.airline.master.geography.service;

import com.codeshare.airline.platform.core.dto.master.georegion.LocationSearchDTO;

import java.util.List;

public interface LocationSearchService {

    List<LocationSearchDTO> search(String keyword);
}