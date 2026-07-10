package com.codeshare.airline.tenant.management.service.impl;

import com.codeshare.airline.core.dto.tenant.IdentityProviderConfigDTO;
import com.codeshare.airline.core.dto.tenant.OidcConfigDTO;
import com.codeshare.airline.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.core.enums.common.TenantPlan;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.identity.OidcConfigEntity;
import com.codeshare.airline.tenant.entities.identity.OidcIdentityProviderEntity;
import com.codeshare.airline.tenant.integration.identity.IdentityBootstrapClient;
import com.codeshare.airline.tenant.mappers.TenantMapper;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repository;
    private final TenantMapper mapper;
    private final IdentityBootstrapClient identityBootstrapClient;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public TenantDTO create(TenantDTO dto) {
        String tenantCode = normalizeTenantCode(dto.getTenantCode());
        if (repository.existsByTenantCode(tenantCode)) {
            throw new IllegalStateException("Tenant code already exists: " + tenantCode);
        }
        dto.setTenantCode(tenantCode);

        Tenant entity = mapper.toEntity(dto);
        applyIdentityProviderConfig(entity, dto);

        Tenant saved = repository.saveAndFlush(entity);
        identityBootstrapClient.bootstrapTenant(saved.getTenantCode(), saved.getTenantCode());
        return toTenantDto(saved);
    }

    @Override
    public TenantDTO update(UUID id, TenantDTO dto) {
        Tenant entity = repository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getPlan() != null) entity.setPlan(TenantPlan.valueOf(dto.getPlan()));
        if (dto.getSubscriptionStart() != null) entity.setSubscriptionStart(dto.getSubscriptionStart());
        if (dto.getSubscriptionEnd() != null) entity.setSubscriptionEnd(dto.getSubscriptionEnd());
        if (dto.getTrial() != null) entity.setTrial(dto.getTrial());
        if (dto.getContactEmail() != null) entity.setContactEmail(dto.getContactEmail());
        if (dto.getContactPhone() != null) entity.setContactPhone(dto.getContactPhone());
        if (dto.getLogoUrl() != null) entity.setLogoUrl(dto.getLogoUrl());
        if (dto.getRegion() != null) entity.setRegion(dto.getRegion());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        applyIdentityProviderConfig(entity, dto);

        return toTenantDto(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public TenantDTO getById(UUID id) {
        return repository.findById(id)
                .map(this::toTenantDto)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TenantDTO getByCode(String code) {
        return repository.findByTenantCode(normalizeTenantCode(code))
                .map(this::toTenantDto)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantDTO> getAll() {
        return repository.findAll().stream().map(this::toTenantDto).toList();
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new CSMResourceNotFoundException("Tenant not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantAuthContextDTO getAuthContextByCode(String tenantCode) {
        Tenant tenant = repository.findByTenantCode(normalizeTenantCode(tenantCode))
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + tenantCode));

        return TenantAuthContextDTO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .tenantCode(tenant.getTenantCode())
                .status(tenant.getStatus())
                .logoUrl(tenant.getLogoUrl())
                .region(tenant.getRegion())
                .identityProviders(
                        tenant.getIdentityProviders().stream()
                                .filter(OidcIdentityProviderEntity::isEnabled)
                                .sorted(Comparator.comparingInt(OidcIdentityProviderEntity::getPriority))
                                .map(this::toIdentityProviderConfig)
                                .toList()
                )
                .build();
    }

    private IdentityProviderConfigDTO toIdentityProviderConfig(OidcIdentityProviderEntity provider) {
        return IdentityProviderConfigDTO.builder()
                .authSource(provider.getAuthSource())
                .enabled(provider.isEnabled())
                .priority(provider.getPriority())
                .providerId(provider.getAuthSource().name().toLowerCase(Locale.ROOT))
                .oidcConfig(toOidcConfig(provider.getOidcConfig()))
                .build();
    }

    private OidcConfigDTO toOidcConfig(OidcConfigEntity entity) {
        if (entity == null) {
            return null;
        }
        return OidcConfigDTO.builder()
                .issuerUri(entity.getIssuerUri())
                .authorizationUri(entity.getAuthorizationUri())
                .tokenUri(entity.getTokenUri())
                .jwkSetUri(entity.getJwkSetUri())
                .clientId(entity.getClientId())
                .clientSecretRef(entity.getClientSecretRef())
                .redirectUri(entity.getRedirectUri())
                .scopes(entity.getScopes())
                .enforceRedirectUri(entity.isEnforceRedirectUri())
                .build();
    }

    private TenantDTO toTenantDto(Tenant tenant) {
        TenantDTO dto = mapper.toDTO(tenant);
        tenant.getIdentityProviders().stream()
                .filter(OidcIdentityProviderEntity::isEnabled)
                .sorted(Comparator.comparingInt(OidcIdentityProviderEntity::getPriority))
                .findFirst()
                .ifPresent(provider -> {
                    dto.setAuthSource(provider.getAuthSource());
                    dto.setOidcConfig(toOidcConfig(provider.getOidcConfig()));
                });
        return dto;
    }

    private void applyIdentityProviderConfig(Tenant tenant, TenantDTO dto) {
        AuthSource requestedAuthSource = dto.getAuthSource();
        if (requestedAuthSource == null) {
            ensureDefaultIdentityProvider(tenant);
            return;
        }

        tenant.getIdentityProviders().clear();

        OidcIdentityProviderEntity provider = new OidcIdentityProviderEntity();
        provider.setTenant(tenant);
        provider.setAuthSource(requestedAuthSource);
        provider.setEnabled(true);
        provider.setPriority(1);

        if (requestedAuthSource != AuthSource.INTERNAL) {
            provider.setOidcConfig(toOidcConfigEntity(dto.getOidcConfig(), provider, requestedAuthSource));
        }

        tenant.getIdentityProviders().add(provider);
    }

    private void ensureDefaultIdentityProvider(Tenant tenant) {
        if (tenant.getIdentityProviders().stream().anyMatch(OidcIdentityProviderEntity::isEnabled)) {
            return;
        }

        OidcIdentityProviderEntity provider = new OidcIdentityProviderEntity();
        provider.setTenant(tenant);
        provider.setAuthSource(AuthSource.INTERNAL);
        provider.setEnabled(true);
        provider.setPriority(1);
        tenant.getIdentityProviders().add(provider);
    }

    private OidcConfigEntity toOidcConfigEntity(
            OidcConfigDTO dto,
            OidcIdentityProviderEntity provider,
            AuthSource authSource
    ) {
        if (authSource == AuthSource.LDAP) {
            return OidcConfigEntity.builder()
                    .identityProvider(provider)
                    .issuerUri(dto != null ? dto.getIssuerUri() : null)
                    .scopes(dto != null ? dto.getScopes() : null)
                    .redirectUri(dto != null ? dto.getRedirectUri() : null)
                    .clientSecretRef(dto != null ? dto.getClientSecretRef() : null)
                    .build();
        }

        if (authSource.isOidc()) {
            if (dto == null) {
                throw new IllegalArgumentException("OIDC configuration is required for " + authSource);
            }

            return OidcConfigEntity.builder()
                    .identityProvider(provider)
                    .issuerUri(dto.getIssuerUri())
                    .authorizationUri(dto.getAuthorizationUri())
                    .tokenUri(dto.getTokenUri())
                    .jwkSetUri(dto.getJwkSetUri())
                    .clientId(dto.getClientId())
                    .clientSecretRef(dto.getClientSecretRef())
                    .redirectUri(dto.getRedirectUri())
                    .grantType("authorization_code")
                    .clientAuthMethod("client_secret_post")
                    .scopes(dto.getScopes())
                    .enforceRedirectUri(dto.isEnforceRedirectUri())
                    .build();
        }

        return null;
    }

    private String normalizeTenantCode(String tenantCode) {
        if (tenantCode == null || tenantCode.isBlank()) {
            throw new IllegalArgumentException("Tenant code is required");
        }
        return tenantCode.trim().toUpperCase();
    }
}
