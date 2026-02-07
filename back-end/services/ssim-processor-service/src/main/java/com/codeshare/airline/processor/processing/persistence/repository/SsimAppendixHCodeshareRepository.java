package com.codeshare.airline.processor.processing.persistence.repository;


import com.codeshare.airline.processor.model.raw.SsimAppendixHCodeshare;
import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SsimAppendixHCodeshareRepository
        extends JpaRepository<SsimAppendixHCodeshare, UUID> {
    List<SsimAppendixHCodeshare> findByOperatingLeg_SsimR1Header(SsimR1HeaderRecord header);
}
