package com.codeshare.airline.master.geography.service;

import com.codeshare.airline.platform.core.dto.master.georegion.CityDTO;
import com.codeshare.airline.master.common.base.BaseService;
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