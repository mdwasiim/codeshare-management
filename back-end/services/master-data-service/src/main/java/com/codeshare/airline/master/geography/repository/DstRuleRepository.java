package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DstRuleRepository
        extends CSMDataBaseRepository<TimezoneDLS, Long> {
    boolean existsByTimezone_TzIdentifierAndEffectiveFrom(String tzIdentifier, LocalDateTime effectiveFrom);

    @Query("""
            SELECT d FROM TimezoneDLS d
            WHERE d.timezone.id = :timezoneId
              AND :localDateTime >= d.dstStart
              AND :localDateTime < d.dstEnd
              AND :localDateTime >= d.effectiveFrom
              AND (d.effectiveTo IS NULL OR :localDateTime <= d.effectiveTo)
            ORDER BY d.effectiveFrom DESC
            """)
    List<TimezoneDLS> findActiveDstPeriods(
            @Param("timezoneId") Long timezoneId,
            @Param("localDateTime") LocalDateTime localDateTime
    );
}
