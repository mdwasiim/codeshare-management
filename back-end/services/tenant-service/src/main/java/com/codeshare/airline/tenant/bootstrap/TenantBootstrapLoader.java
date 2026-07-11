package com.codeshare.airline.tenant.bootstrap;

import com.codeshare.airline.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.common.TenantPlan;
import com.codeshare.airline.core.enums.common.TenantStatus;
import com.codeshare.airline.core.enums.master.airline.CodeshareAgreementStatus;
import com.codeshare.airline.core.enums.master.airline.CodeshareAgreementType;
import com.codeshare.airline.tenant.entities.identity.OidcIdentityProviderEntity;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.integration.master.MasterDataAirlineClient;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Order(100)
@RequiredArgsConstructor
public class TenantBootstrapLoader implements CommandLineRunner {

    private static final String TENANT_CODE = "QR";
    private static final LocalDate EFFECTIVE_FROM = LocalDate.of(2025, 1, 1);

    private final TenantRepository tenantRepository;
    private final CodesharePartnerRepository codesharePartnerRepository;
    private final MasterDataAirlineClient masterDataAirlineClient;

    @Override
    public void run(String... args) {
        Tenant tenant = ensureTenant();
        syncPartnerMappings(tenant);
    }

    private Tenant ensureTenant() {
        Tenant tenant = tenantRepository.findByTenantCode(TENANT_CODE)
                .orElseGet(() -> {
                    Tenant item = Tenant.builder().build();
                    item.setTenantCode(TENANT_CODE);
                    return item;
                });

        tenant.setName("Qatar Airways");
        tenant.setDescription("Host airline tenant for Qatar Airways");
        tenant.setStatus(TenantStatus.ACTIVE);
        tenant.setPlan(TenantPlan.ENTERPRISE);
        tenant.setSubscriptionStart(LocalDateTime.of(2025, 1, 1, 0, 0));
        tenant.setSubscriptionEnd(LocalDateTime.of(2035, 1, 1, 0, 0));
        tenant.setTrial(false);
        tenant.setContactEmail("platform-admin@qatarairways.com");
        tenant.setContactPhone("+974 4022 6000");
        tenant.setLogoUrl("https://www.qatarairways.com/content/dam/images/renditions/qatar-airways-logo.svg");
        tenant.setRegion("Qatar");
        tenant.setCreatedBy("BOOTSTRAP");
        tenant.setUpdatedBy("BOOTSTRAP");

        ensureInternalIdentityProvider(tenant);

        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant bootstrap ensured for tenant {}", saved.getTenantCode());
        return saved;
    }

    private void ensureInternalIdentityProvider(Tenant tenant) {
        boolean exists = tenant.getIdentityProviders().stream()
                .anyMatch(provider -> provider.getAuthSource() == AuthSource.INTERNAL);
        if (exists) {
            return;
        }

        OidcIdentityProviderEntity provider = new OidcIdentityProviderEntity();
        provider.setTenant(tenant);
        provider.setAuthSource(AuthSource.INTERNAL);
        provider.setEnabled(true);
        provider.setPriority(1);
        provider.setCreatedBy("BOOTSTRAP");
        provider.setUpdatedBy("BOOTSTRAP");

        tenant.getIdentityProviders().add(provider);
    }

    private void syncPartnerMappings(Tenant tenant) {
        AirlineCarrierDTO homeAirline = resolveAirline("QR");
        if (homeAirline == null) {
            log.warn("Skipping tenant partner bootstrap. Home airline QR is not available in master-data-service");
            return;
        }

        List<String> partnerCodes = List.of("BA", "AA", "VA");
        int order = 1;
        for (String partnerCode : partnerCodes) {
            AirlineCarrierDTO partnerAirline = resolveAirline(partnerCode);
            if (partnerAirline == null) {
                log.warn("Skipping partner mapping {} for tenant {} because the airline does not exist in master-data-service", partnerCode, tenant.getTenantCode());
                continue;
            }
            ensurePartnerMapping(tenant.getId(), homeAirline, partnerAirline, order++);
        }
    }

    private AirlineCarrierDTO resolveAirline(String iataCode) {
        try {
            return masterDataAirlineClient.getByIataCode(iataCode);
        } catch (Exception ex) {
            log.warn("Unable to resolve airline {} from master-data-service", iataCode, ex);
            return null;
        }
    }

    private void ensurePartnerMapping(UUID tenantId, AirlineCarrierDTO homeAirline, AirlineCarrierDTO partnerAirline, int displayOrder) {
        if (codesharePartnerRepository.existsByTenantIdAndHomeAirlineIdAndPartnerAirlineId(tenantId, homeAirline.getId(), partnerAirline.getId())) {
            return;
        }

        CodesharePartner item = new CodesharePartner();
        item.setTenantId(tenantId);
        item.setHomeAirlineId(homeAirline.getId());
        item.setPartnerAirlineId(partnerAirline.getId());
        item.setAgreementNumber(homeAirline.getIataCode() + "-" + partnerAirline.getIataCode() + "-2025");
        item.setAgreementType(CodeshareAgreementType.BILATERAL);
        item.setAgreementStatus(CodeshareAgreementStatus.ACTIVE);
        item.setActive(Boolean.TRUE);
        item.setDisplayOrder(displayOrder);
        item.setDescription(homeAirline.getDisplayName() + " and " + partnerAirline.getDisplayName() + " tenant partner mapping.");
        item.setRecordStatus(RecordStatus.ACTIVE);
        item.setEffectiveFrom(EFFECTIVE_FROM);
        item.setCreatedBy("BOOTSTRAP");
        item.setUpdatedBy("BOOTSTRAP");
        codesharePartnerRepository.save(item);
    }
}
