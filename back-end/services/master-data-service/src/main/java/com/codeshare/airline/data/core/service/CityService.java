package com.codeshare.airline.data.core.service;

import com.codeshare.airline.core.dto.georegion.CityDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CityService
        extends BaseService<CityDTO, UUID> {

    List<CityDTO> getByCountry(UUID countryId);

    List<CityDTO> getByState(UUID stateId);

    Page<CityDTO> search(String keyword, Pageable pageable);

    List<CityDTO> autocomplete(String keyword);
}