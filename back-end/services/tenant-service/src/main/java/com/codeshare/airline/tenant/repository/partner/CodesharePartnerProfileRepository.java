package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CodesharePartnerProfileRepository extends CSMDataBaseRepository<CodesharePartnerProfile, Long> {

    boolean existsByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    Optional<CodesharePartnerProfile> findByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    @Query("""
            select p from CodesharePartnerProfile p
            where p.partner.id = :partnerId
              and p.active = true
              and p.recordStatus = 'ACTIVE'
              and (p.effectiveFrom is null or p.effectiveFrom <= :businessDate)
              and (p.effectiveTo is null or p.effectiveTo >= :businessDate)
            order by p.priority asc, p.displayOrder asc, p.id asc
            """)
    Optional<CodesharePartnerProfile> findEffectiveProfile(
            @Param("partnerId") Long partnerId,
            @Param("businessDate") LocalDate businessDate
    );
}
