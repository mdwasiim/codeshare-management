package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.ssim.SsimTrailerEntity;

import java.util.UUID;

public interface SsimTrailerRepository
        extends CSMDataBaseRepository<SsimTrailerEntity, UUID> {

}
