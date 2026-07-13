package com.codeshare.airline.tenant.bootstrap;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.common.TenantPlan;
import com.codeshare.airline.platform.core.enums.common.TenantStatus;
import com.codeshare.airline.platform.core.enums.master.airline.CodeshareAgreementStatus;
import com.codeshare.airline.platform.core.enums.master.airline.CodeshareAgreementType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.AuthenticationType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CodeshareAgreementCategory;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.DistributionMode;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.InventorySharingType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.MessageFormat;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.PartnerType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.TransportType;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.tenant.entities.identity.OidcIdentityProviderEntity;
import com.codeshare.airline.tenant.entities.identity.OidcConfigEntity;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.ingestion.ScheduleIngestionChannelEntity;
import com.codeshare.airline.tenant.entities.ingestion.ScheduleIngestionProfileEntity;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.integration.master.MasterDataAirlineClient;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerDistributionProfile;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerProfile;
import com.codeshare.airline.tenant.repository.ingestion.ScheduleIngestionProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerCommunicationProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerDistributionProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Order(100)
@RequiredArgsConstructor
@Transactional
public class TenantBootstrapLoader implements CommandLineRunner {

    private static final String BOOTSTRAP_USER = "BOOTSTRAP";
    private static final String TENANT_CODE = "QR";
    private static final LocalDate EFFECTIVE_FROM = LocalDate.of(2025, 1, 1);
    private static final LocalDate EFFECTIVE_TO = LocalDate.of(2035, 1, 1);

    private final TenantRepository tenantRepository;
    private final ScheduleIngestionProfileRepository scheduleIngestionProfileRepository;
    private final CodesharePartnerRepository codesharePartnerRepository;
    private final CodesharePartnerProfileRepository codesharePartnerProfileRepository;
    private final CodesharePartnerCommunicationProfileRepository codesharePartnerCommunicationProfileRepository;
    private final CodesharePartnerDistributionProfileRepository codesharePartnerDistributionProfileRepository;
    private final MasterDataAirlineClient masterDataAirlineClient;

    @Override
    public void run(String... args) {
        Tenant tenant = ensureTenant();
        ensureIngestionProfile(tenant);
        syncPartnerMappings(tenant);
        log.info("Tenant bootstrap fully ensured for tenant {}", tenant.getTenantCode());
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
        tenant.setCreatedBy(BOOTSTRAP_USER);
        tenant.setUpdatedBy(BOOTSTRAP_USER);
        tenant.setActive(true);
        tenant.setDeleted(false);

        ensureInternalIdentityProvider(tenant);
        ensureBootstrapOidcProvider(tenant);

        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant bootstrap ensured for tenant {}", saved.getTenantCode());
        return saved;
    }

    private void ensureInternalIdentityProvider(Tenant tenant) {
        OidcIdentityProviderEntity provider = tenant.getIdentityProviders().stream()
                .filter(existing -> existing.getAuthSource() == AuthSource.INTERNAL)
                .findFirst()
                .orElseGet(() -> {
                    OidcIdentityProviderEntity item = new OidcIdentityProviderEntity();
                    item.setTenant(tenant);
                    item.setAuthSource(AuthSource.INTERNAL);
                    tenant.getIdentityProviders().add(item);
                    return item;
                });

        provider.setTenant(tenant);
        provider.setAuthSource(AuthSource.INTERNAL);
        provider.setEnabled(true);
        provider.setPriority(1);
        provider.setCreatedBy(BOOTSTRAP_USER);
        provider.setUpdatedBy(BOOTSTRAP_USER);
    }

    private void ensureBootstrapOidcProvider(Tenant tenant) {
        OidcIdentityProviderEntity provider = tenant.getIdentityProviders().stream()
                .filter(existing -> existing.getAuthSource() == AuthSource.OIDC_GENERIC)
                .findFirst()
                .orElseGet(() -> {
                    OidcIdentityProviderEntity item = new OidcIdentityProviderEntity();
                    item.setTenant(tenant);
                    item.setAuthSource(AuthSource.OIDC_GENERIC);
                    tenant.getIdentityProviders().add(item);
                    return item;
                });

        provider.setTenant(tenant);
        provider.setAuthSource(AuthSource.OIDC_GENERIC);
        provider.setEnabled(false);
        provider.setPriority(100);
        provider.setCreatedBy(BOOTSTRAP_USER);
        provider.setUpdatedBy(BOOTSTRAP_USER);

        OidcConfigEntity oidcConfig = provider.getOidcConfig();
        if (oidcConfig == null) {
            oidcConfig = new OidcConfigEntity();
            oidcConfig.setIdentityProvider(provider);
            provider.setOidcConfig(oidcConfig);
        }

        oidcConfig.setIdentityProvider(provider);
        oidcConfig.setIssuerUri("https://identity.codeshare.local/realms/qr");
        oidcConfig.setAuthorizationUri("https://identity.codeshare.local/realms/qr/protocol/openid-connect/auth");
        oidcConfig.setTokenUri("https://identity.codeshare.local/realms/qr/protocol/openid-connect/token");
        oidcConfig.setJwkSetUri("https://identity.codeshare.local/realms/qr/protocol/openid-connect/certs");
        oidcConfig.setClientId("codeshare-qr-portal");
        oidcConfig.setClientSecretRef("tenant/qr/identity/oidc-client-secret");
        oidcConfig.setRedirectUri("http://localhost:4201/auth/callback");
        oidcConfig.setGrantType("authorization_code");
        oidcConfig.setClientAuthMethod("client_secret_post");
        oidcConfig.setScopes("openid profile email");
        oidcConfig.setEnforceRedirectUri(true);
        oidcConfig.setCreatedBy(BOOTSTRAP_USER);
        oidcConfig.setUpdatedBy(BOOTSTRAP_USER);
    }

    private void ensureIngestionProfile(Tenant tenant) {
        ScheduleIngestionProfileEntity profile = scheduleIngestionProfileRepository.findWithChannelsByTenantCode(tenant.getTenantCode())
                .orElseGet(ScheduleIngestionProfileEntity::new);

        profile.setTenant(tenant);
        profile.setSourceSystem("CODESHARE_PLATFORM");
        profile.setEnabled(Boolean.TRUE);
        profile.setPollIntervalMs(300000L);
        profile.setCreatedBy(BOOTSTRAP_USER);
        profile.setUpdatedBy(BOOTSTRAP_USER);
        reconcileChannels(
                profile,
                List.of(
                        buildSftpIngestionChannel(MessageType.SSIM, 1, "/inbound/ssim", "*.ssim"),
                        buildMqIngestionChannel(MessageType.ASM, 2, "schedule.asm.inbound.qr"),
                        buildMqIngestionChannel(MessageType.SSM, 3, "schedule.ssm.inbound.qr")
                )
        );

        scheduleIngestionProfileRepository.save(profile);
    }

    private void reconcileChannels(
            ScheduleIngestionProfileEntity profile,
            List<ScheduleIngestionChannelEntity> expectedChannels
    ) {
        Map<String, ScheduleIngestionChannelEntity> existingByKey = new HashMap<>();
        for (ScheduleIngestionChannelEntity channel : profile.getChannels()) {
            existingByKey.put(channelKey(channel.getMessageType(), channel.getSourceType()), channel);
        }

        Set<String> expectedKeys = new HashSet<>();
        for (ScheduleIngestionChannelEntity expected : expectedChannels) {
            String key = channelKey(expected.getMessageType(), expected.getSourceType());
            expectedKeys.add(key);

            ScheduleIngestionChannelEntity existing = existingByKey.get(key);
            if (existing != null) {
                applyChannelConfiguration(existing, expected);
            } else {
                profile.addChannel(expected);
            }
        }

        profile.getChannels().removeIf(channel -> !expectedKeys.contains(channelKey(channel.getMessageType(), channel.getSourceType())));
    }

    private void applyChannelConfiguration(
            ScheduleIngestionChannelEntity target,
            ScheduleIngestionChannelEntity source
    ) {
        target.setProfile(source.getProfile() != null ? source.getProfile() : target.getProfile());
        target.setMessageType(source.getMessageType());
        target.setSourceType(source.getSourceType());
        target.setHost(source.getHost());
        target.setPort(source.getPort());
        target.setUsername(source.getUsername());
        target.setPasswordEncrypted(source.getPasswordEncrypted());
        target.setRemoteDirectory(source.getRemoteDirectory());
        target.setProtocol(source.getProtocol());
        target.setMailbox(source.getMailbox());
        target.setMailDelayMs(source.getMailDelayMs());
        target.setMailUnseenOnly(source.getMailUnseenOnly());
        target.setMailDelete(source.getMailDelete());
        target.setMailPeek(source.getMailPeek());
        target.setMailMoveTo(source.getMailMoveTo());
        target.setBrokerUrl(source.getBrokerUrl());
        target.setQueueName(source.getQueueName());
        target.setTopicName(source.getTopicName());
        target.setConcurrentConsumers(source.getConcurrentConsumers());
        target.setMaxConcurrentConsumers(source.getMaxConcurrentConsumers());
        target.setAsyncConsumer(source.getAsyncConsumer());
        target.setReceiveTimeoutMs(source.getReceiveTimeoutMs());
        target.setMaxMessagesPerTask(source.getMaxMessagesPerTask());
        target.setFileNoop(source.getFileNoop());
        target.setFileDelete(source.getFileDelete());
        target.setFileIncludePattern(source.getFileIncludePattern());
        target.setFileExcludePattern(source.getFileExcludePattern());
        target.setFileReadLock(source.getFileReadLock());
        target.setFileReadLockMinAge(source.getFileReadLockMinAge());
        target.setFileReadLockTimeout(source.getFileReadLockTimeout());
        target.setFileReadLockCheckInterval(source.getFileReadLockCheckInterval());
        target.setFilePollDelayMs(source.getFilePollDelayMs());
        target.setFileInitialDelayMs(source.getFileInitialDelayMs());
        target.setFileMove(source.getFileMove());
        target.setFileMoveFailed(source.getFileMoveFailed());
        target.setFilePreMove(source.getFilePreMove());
        target.setFileIdempotent(source.getFileIdempotent());
        target.setFileIdempotentKey(source.getFileIdempotentKey());
        target.setFileCharset(source.getFileCharset());
        target.setMaxMessagesPerPoll(source.getMaxMessagesPerPoll());
        target.setRecursive(source.getRecursive());
        target.setBridgeErrorHandler(source.getBridgeErrorHandler());
        target.setDisconnect(source.getDisconnect());
        target.setBinary(source.getBinary());
        target.setPassiveMode(source.getPassiveMode());
        target.setReconnectDelayMs(source.getReconnectDelayMs());
        target.setMaximumReconnectAttempts(source.getMaximumReconnectAttempts());
        target.setSoTimeoutMs(source.getSoTimeoutMs());
        target.setRetryAttempts(source.getRetryAttempts());
        target.setRetryDelayMs(source.getRetryDelayMs());
        target.setEnabled(source.getEnabled());
        target.setPriority(source.getPriority());
        target.setUpdatedBy(BOOTSTRAP_USER);
    }

    private String channelKey(MessageType messageType, SourceType sourceType) {
        return messageType.name() + ":" + sourceType.name();
    }

    private void syncPartnerMappings(Tenant tenant) {
        AirlineCarrierDTO homeAirline = resolveRequiredAirline("QR");

        List<String> partnerCodes = List.of("BA", "AA", "VA");
        int order = 1;
        for (String partnerCode : partnerCodes) {
            AirlineCarrierDTO partnerAirline = resolveRequiredAirline(partnerCode);
            CodesharePartner partner = ensurePartnerMapping(tenant.getId(), homeAirline, partnerAirline, order++);
            ensurePartnerProfiles(partner, homeAirline, partnerAirline);
        }
    }

    private AirlineCarrierDTO resolveRequiredAirline(String iataCode) {
        try {
            return masterDataAirlineClient.getByIataCode(iataCode);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to resolve airline " + iataCode + " from master-data-service during tenant bootstrap", ex);
        }
    }

    private CodesharePartner ensurePartnerMapping(Long tenantId, AirlineCarrierDTO homeAirline, AirlineCarrierDTO partnerAirline, int displayOrder) {
        CodesharePartner item = codesharePartnerRepository
                .findByTenantIdAndHomeAirlineIdAndPartnerAirlineId(tenantId, homeAirline.getId(), partnerAirline.getId())
                .orElseGet(CodesharePartner::new);
        item.setTenantId(tenantId);
        item.setHomeAirlineId(homeAirline.getId());
        item.setPartnerAirlineId(partnerAirline.getId());
        item.setAgreementNumber(homeAirline.getIataCode() + "-" + partnerAirline.getIataCode() + "-2025");
        item.setAgreementType(CodeshareAgreementType.BILATERAL);
        item.setAgreementStatus(CodeshareAgreementStatus.ACTIVE);
        item.setActive(Boolean.TRUE);
        item.setDisplayOrder(displayOrder);
        item.setDescription(homeAirline.getDisplayName() + " and " + partnerAirline.getDisplayName() + " tenant partner mapping.");
        item.setRemarks("Bootstrap-managed bilateral partner mapping for schedule, inventory, and operational distribution.");
        item.setRecordStatus(RecordStatus.ACTIVE);
        item.setEffectiveFrom(EFFECTIVE_FROM);
        item.setEffectiveTo(EFFECTIVE_TO);
        item.setCreatedBy(BOOTSTRAP_USER);
        item.setUpdatedBy(BOOTSTRAP_USER);
        return codesharePartnerRepository.save(item);
    }

    private void ensurePartnerProfiles(CodesharePartner partner, AirlineCarrierDTO homeAirline, AirlineCarrierDTO partnerAirline) {
        String codePrefix = homeAirline.getIataCode() + "-" + partnerAirline.getIataCode();

        CodesharePartnerProfile partnerProfile = codesharePartnerProfileRepository
                .findByPartner_IdAndProfileCode(partner.getId(), codePrefix + "-CORE")
                .orElseGet(CodesharePartnerProfile::new);
        partnerProfile.setPartner(partner);
        partnerProfile.setProfileCode(codePrefix + "-CORE");
        partnerProfile.setProfileName(partnerAirline.getDisplayName() + " Core Codeshare Profile");
        partnerProfile.setPartnerType(PartnerType.RECIPROCAL);
        partnerProfile.setAgreementCategory(CodeshareAgreementCategory.BILATERAL);
        partnerProfile.setInventorySharingType(InventorySharingType.FREE_SALE);
        partnerProfile.setPriority(partner.getDisplayOrder());
        partnerProfile.setAutoAcceptScheduleChanges(Boolean.FALSE);
        partnerProfile.setProrationApplicable(Boolean.TRUE);
        partnerProfile.setETicketAllowed(Boolean.TRUE);
        partnerProfile.setActive(Boolean.TRUE);
        partnerProfile.setDisplayOrder(partner.getDisplayOrder());
        partnerProfile.setDescription("Bootstrap-managed operating profile for " + partnerAirline.getDisplayName() + ".");
        partnerProfile.setRemarks("Used for partner-level schedule evaluation and default inventory sharing rules.");
        partnerProfile.setRecordStatus(RecordStatus.ACTIVE);
        partnerProfile.setEffectiveFrom(EFFECTIVE_FROM);
        partnerProfile.setEffectiveTo(EFFECTIVE_TO);
        partnerProfile.setCreatedBy(BOOTSTRAP_USER);
        partnerProfile.setUpdatedBy(BOOTSTRAP_USER);
        codesharePartnerProfileRepository.save(partnerProfile);

        CodesharePartnerCommunicationProfile communicationProfile = codesharePartnerCommunicationProfileRepository
                .findByPartner_IdAndProfileCode(partner.getId(), codePrefix + "-COMM")
                .orElseGet(CodesharePartnerCommunicationProfile::new);
        communicationProfile.setPartner(partner);
        communicationProfile.setProfileCode(codePrefix + "-COMM");
        communicationProfile.setProfileName(partnerAirline.getDisplayName() + " Schedule Communication Profile");
        communicationProfile.setProtocol(CommunicationProtocol.API);
        communicationProfile.setTransportType(TransportType.HTTPS);
        communicationProfile.setMessageFormat(MessageFormat.JSON);
        communicationProfile.setAuthenticationType(AuthenticationType.API_KEY);
        communicationProfile.setEndpointUrl("https://api." + partnerAirline.getIataCode().toLowerCase() + ".codeshare.local/schedules");
        communicationProfile.setUsername(homeAirline.getIataCode().toLowerCase() + "_schedule_exchange");
        communicationProfile.setCredentialAlias("tenant/" + TENANT_CODE.toLowerCase() + "/partners/" + partnerAirline.getIataCode().toLowerCase() + "/api-key");
        communicationProfile.setConnectionTimeout(10000);
        communicationProfile.setReadTimeout(30000);
        communicationProfile.setRetryCount(5);
        communicationProfile.setCompressionEnabled(Boolean.TRUE);
        communicationProfile.setEncryptionEnabled(Boolean.TRUE);
        communicationProfile.setActive(Boolean.TRUE);
        communicationProfile.setDisplayOrder(partner.getDisplayOrder());
        communicationProfile.setDescription("Bootstrap-managed partner communication profile for schedule exchange.");
        communicationProfile.setRemarks("Primary communication route for SSIM, ASM, and SSM exchange with " + partnerAirline.getDisplayName() + ".");
        communicationProfile.setRecordStatus(RecordStatus.ACTIVE);
        communicationProfile.setEffectiveFrom(EFFECTIVE_FROM);
        communicationProfile.setEffectiveTo(EFFECTIVE_TO);
        communicationProfile.setCreatedBy(BOOTSTRAP_USER);
        communicationProfile.setUpdatedBy(BOOTSTRAP_USER);
        codesharePartnerCommunicationProfileRepository.save(communicationProfile);

        ensureDistributionProfile(partner, partnerAirline, codePrefix, MessageType.SSIM, 1, DistributionMode.SCHEDULED, CommunicationProtocol.SFTP, Boolean.FALSE);
        ensureDistributionProfile(partner, partnerAirline, codePrefix, MessageType.ASM, 2, DistributionMode.REAL_TIME, CommunicationProtocol.MQ, Boolean.TRUE);
        ensureDistributionProfile(partner, partnerAirline, codePrefix, MessageType.SSM, 3, DistributionMode.REAL_TIME, CommunicationProtocol.MQ, Boolean.TRUE);
    }

    private void ensureDistributionProfile(
            CodesharePartner partner,
            AirlineCarrierDTO partnerAirline,
            String codePrefix,
            MessageType messageType,
            int displayOrder,
            DistributionMode distributionMode,
            CommunicationProtocol distributionChannel,
            Boolean realTimeEnabled
    ) {
        String profileCode = codePrefix + "-DIST-" + messageType.name();
        CodesharePartnerDistributionProfile distributionProfile = codesharePartnerDistributionProfileRepository
                .findByPartner_IdAndProfileCode(partner.getId(), profileCode)
                .orElseGet(CodesharePartnerDistributionProfile::new);
        distributionProfile.setPartner(partner);
        distributionProfile.setProfileCode(profileCode);
        distributionProfile.setProfileName(partnerAirline.getDisplayName() + " " + messageType.name() + " Distribution Profile");
        distributionProfile.setDistributionChannel(distributionChannel);
        distributionProfile.setDistributionMode(distributionMode);
        distributionProfile.setMessageType(messageType);
        distributionProfile.setRealTimeEnabled(realTimeEnabled);
        distributionProfile.setAcknowledgementRequired(Boolean.TRUE);
        distributionProfile.setRetryEnabled(Boolean.TRUE);
        distributionProfile.setRetryCount(5);
        distributionProfile.setActive(Boolean.TRUE);
        distributionProfile.setDisplayOrder(displayOrder);
        distributionProfile.setDescription("Bootstrap-managed " + messageType.name() + " distribution profile for " + partnerAirline.getDisplayName() + ".");
        distributionProfile.setRemarks("Default outbound " + messageType.name() + " routing profile for bootstrap tenant data.");
        distributionProfile.setRecordStatus(RecordStatus.ACTIVE);
        distributionProfile.setEffectiveFrom(EFFECTIVE_FROM);
        distributionProfile.setEffectiveTo(EFFECTIVE_TO);
        distributionProfile.setCreatedBy(BOOTSTRAP_USER);
        distributionProfile.setUpdatedBy(BOOTSTRAP_USER);
        codesharePartnerDistributionProfileRepository.save(distributionProfile);
    }

    private ScheduleIngestionChannelEntity buildSftpIngestionChannel(
            MessageType messageType,
            int priority,
            String remoteDirectory,
            String includePattern
    ) {
        ScheduleIngestionChannelEntity channel = new ScheduleIngestionChannelEntity();
        channel.setMessageType(messageType);
        channel.setSourceType(SourceType.SFTP);
        channel.setEnabled(Boolean.TRUE);
        channel.setPriority(priority);
        channel.setHost("sftp.qr.codeshare.local");
        channel.setPort(22);
        channel.setUsername("qr_schedule_ingestion");
        channel.setPasswordEncrypted("vault://tenant/qr/ingestion/sftp-password");
        channel.setRemoteDirectory(remoteDirectory);
        channel.setProtocol("SFTP");
        channel.setFileNoop(Boolean.FALSE);
        channel.setFileDelete(Boolean.FALSE);
        channel.setFileIncludePattern(includePattern);
        channel.setFileExcludePattern("*.tmp,*.bak");
        channel.setFileReadLock("changed");
        channel.setFileReadLockMinAge("30s");
        channel.setFileReadLockTimeout(300000);
        channel.setFileReadLockCheckInterval(10000);
        channel.setFilePollDelayMs(60000);
        channel.setFileInitialDelayMs(5000);
        channel.setFileMove(".archive/${file:name}");
        channel.setFileMoveFailed(".error/${file:name}");
        channel.setFilePreMove(".processing/${file:name}");
        channel.setFileIdempotent(Boolean.TRUE);
        channel.setFileIdempotentKey("${file:name}-${file:size}-${file:modified}");
        channel.setFileCharset("UTF-8");
        channel.setMaxMessagesPerPoll(10);
        channel.setRecursive(Boolean.FALSE);
        channel.setBridgeErrorHandler(Boolean.TRUE);
        channel.setDisconnect(Boolean.TRUE);
        channel.setBinary(Boolean.FALSE);
        channel.setPassiveMode(Boolean.FALSE);
        channel.setReconnectDelayMs(5000);
        channel.setMaximumReconnectAttempts(12);
        channel.setSoTimeoutMs(30000);
        channel.setRetryAttempts(5);
        channel.setRetryDelayMs(10000);
        channel.setCreatedBy(BOOTSTRAP_USER);
        channel.setUpdatedBy(BOOTSTRAP_USER);
        return channel;
    }

    private ScheduleIngestionChannelEntity buildMqIngestionChannel(
            MessageType messageType,
            int priority,
            String queueName
    ) {
        ScheduleIngestionChannelEntity channel = new ScheduleIngestionChannelEntity();
        channel.setMessageType(messageType);
        channel.setSourceType(SourceType.MQ);
        channel.setEnabled(Boolean.TRUE);
        channel.setPriority(priority);
        channel.setProtocol("JMS");
        channel.setBrokerUrl("tcp://mq.codeshare.local:61616");
        channel.setQueueName(queueName);
        channel.setConcurrentConsumers(2);
        channel.setMaxConcurrentConsumers(6);
        channel.setAsyncConsumer(Boolean.TRUE);
        channel.setReceiveTimeoutMs(1000);
        channel.setMaxMessagesPerTask(100);
        channel.setReconnectDelayMs(5000);
        channel.setMaximumReconnectAttempts(12);
        channel.setSoTimeoutMs(30000);
        channel.setRetryAttempts(5);
        channel.setRetryDelayMs(5000);
        channel.setCreatedBy(BOOTSTRAP_USER);
        channel.setUpdatedBy(BOOTSTRAP_USER);
        return channel;
    }
}
