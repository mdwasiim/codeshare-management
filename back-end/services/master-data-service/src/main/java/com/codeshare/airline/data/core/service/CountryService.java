package com.codeshare.airline.data.core.service;

import com.codeshare.airline.core.dto.georegion.CountryDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.UUID;

public interface CountryService
        extends BaseService<CountryDTO, UUID> {
}