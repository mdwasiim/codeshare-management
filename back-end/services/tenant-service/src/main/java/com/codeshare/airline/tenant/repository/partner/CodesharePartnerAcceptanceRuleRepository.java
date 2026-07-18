package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerAcceptanceRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CodesharePartnerAcceptanceRuleRepository extends CSMDataBaseRepository<CodesharePartnerAcceptanceRule, Long> {

    boolean existsByPartner_IdAndMessageType(Long partnerId, MessageType messageType);

    Optional<CodesharePartnerAcceptanceRule> findByPartner_IdAndMessageType(Long partnerId, MessageType messageType);

    @Query("""
            select r from CodesharePartnerAcceptanceRule r
            where r.partner.id = :partnerId
              and r.messageType = :messageType
              and r.active = true
              and r.recordStatus = 'ACTIVE'
              and (r.effectiveFrom is null or r.effectiveFrom <= :businessDate)
              and (r.effectiveTo is null or r.effectiveTo >= :businessDate)
            order by r.displayOrder asc, r.id asc
            """)
    Optional<CodesharePartnerAcceptanceRule> findEffectiveRule(
            @Param("partnerId") Long partnerId,
            @Param("messageType") MessageType messageType,
            @Param("businessDate") LocalDate businessDate
    );
}
