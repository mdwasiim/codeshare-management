package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CodesharePartnerCommunicationProfileRepository extends CSMDataBaseRepository<CodesharePartnerCommunicationProfile, Long> {

    boolean existsByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    Optional<CodesharePartnerCommunicationProfile> findByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    @Query("""
            select p from CodesharePartnerCommunicationProfile p
            where p.partner.id = :partnerId
              and p.protocol = :protocol
              and p.active = true
              and p.recordStatus = 'ACTIVE'
              and (p.effectiveFrom is null or p.effectiveFrom <= :businessDate)
              and (p.effectiveTo is null or p.effectiveTo >= :businessDate)
            order by p.displayOrder asc, p.id asc
            """)
    List<CodesharePartnerCommunicationProfile> findEffectiveProfiles(
            @Param("partnerId") Long partnerId,
            @Param("protocol") CommunicationProtocol protocol,
            @Param("businessDate") LocalDate businessDate
    );
}
