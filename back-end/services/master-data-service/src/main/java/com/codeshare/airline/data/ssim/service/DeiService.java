package com.codeshare.airline.data.ssim.service;

import com.codeshare.airline.core.dto.ssim.DeiDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.Optional;
import java.util.UUID;

public interface DeiService
        extends BaseService<DeiDTO, UUID> {

    Optional<DeiDTO> getByDeiNumber(String deiNumber);
}