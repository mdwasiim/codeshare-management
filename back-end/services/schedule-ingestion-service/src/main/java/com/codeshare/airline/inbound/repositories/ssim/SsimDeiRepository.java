package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.inbound.entities.ssim.SsimDataElementEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface SsimDeiRepository
        extends CSMDataBaseRepository<SsimDataElementEntity, UUID> {

}
