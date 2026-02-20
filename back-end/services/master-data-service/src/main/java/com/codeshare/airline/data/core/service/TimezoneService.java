package com.codeshare.airline.data.core.service;

import com.codeshare.airline.core.dto.georegion.TimezoneDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.UUID;

public interface TimezoneService
        extends BaseService<TimezoneDTO, UUID> {
}