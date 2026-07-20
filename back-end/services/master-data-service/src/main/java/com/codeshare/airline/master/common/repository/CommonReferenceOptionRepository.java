package com.codeshare.airline.master.common.repository;

import com.codeshare.airline.master.common.entities.CommonReferenceOption;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;

public interface CommonReferenceOptionRepository extends CSMDataBaseRepository<CommonReferenceOption, Long> {

    boolean existsByCategoryCodeAndOptionCode(String categoryCode, String optionCode);

    Optional<CommonReferenceOption> findByCategoryCodeAndOptionCode(String categoryCode, String optionCode);

    List<CommonReferenceOption> findByCategoryCodeAndRecordStatusOrderByDisplayOrderAscOptionLabelAsc(String categoryCode, RecordStatus recordStatus);
}
