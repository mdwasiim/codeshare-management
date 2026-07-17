package com.codeshare.airline.master.geography.service;

import com.codeshare.airline.platform.core.dto.master.georegion.CityDTO;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService
        extends BaseService<CityDTO, Long> {

    List<CityDTO> getByCountry(Long countryId);

    List<CityDTO> getByState(Long stateId);

    Page<CityDTO> search(String keyword, Pageable pageable);

    List<CityDTO> autocomplete(String keyword);
}