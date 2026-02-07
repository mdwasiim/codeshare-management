package com.codeshare.airline.processor.processing.persistence.repository;


import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SsimR1HeaderRepository extends JpaRepository<SsimR1HeaderRecord, UUID> {

    Optional<SsimR1HeaderRecord> findByAirlineDesignatorAndDatasetSerialNo(String airlineDesignator,String datasetSerialNo);
}
