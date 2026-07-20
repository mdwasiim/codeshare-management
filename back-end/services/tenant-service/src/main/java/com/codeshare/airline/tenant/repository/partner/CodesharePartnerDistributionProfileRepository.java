package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerDistributionProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CodesharePartnerDistributionProfileRepository extends CSMDataBaseRepository<CodesharePartnerDistributionProfile, Long> {

    boolean existsByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    Optional<CodesharePartnerDistributionProfile> findByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    List<CodesharePartnerDistributionProfile> findByPartner_TenantIdOrderByPartner_IdAscDisplayOrderAscIdAsc(Long tenantId);

    @Query("""
            select p from CodesharePartnerDistributionProfile p
            where p.partner.id = :partnerId
              and p.messageType = :messageType
              and p.active = true
              and p.recordStatus = 'ACTIVE'
              and (p.effectiveFrom is null or p.effectiveFrom <= :businessDate)
              and (p.effectiveTo is null or p.effectiveTo >= :businessDate)
            order by p.displayOrder asc, p.id asc
            """)
    List<CodesharePartnerDistributionProfile> findEffectiveProfiles(
            @Param("partnerId") Long partnerId,
            @Param("messageType") MessageType messageType,
            @Param("businessDate") LocalDate businessDate
    );
}
