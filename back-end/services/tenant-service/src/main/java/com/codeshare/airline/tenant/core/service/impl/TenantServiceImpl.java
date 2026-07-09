package com.codeshare.airline.tenant.core.service.impl;

import com.codeshare.airline.core.dto.tenant.IdentityProviderConfigDTO;
import com.codeshare.airline.core.dto.tenant.OidcConfigDTO;
import com.codeshare.airline.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.enums.common.TenantPlan;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.tenant.authpolicy.entities.OidcConfigEntity;
import com.codeshare.airline.tenant.authpolicy.entities.OidcIdentityProviderEntity;
import com.codeshare.airline.tenant.core.entities.Tenant;
import com.codeshare.airline.tenant.core.mappers.TenantMapper;
import com.codeshare.airline.tenant.core.repository.TenantRepository;
import com.codeshare.airline.tenant.core.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repository;
    private final TenantMapper mapper;

    @Override
    public TenantDTO create(TenantDTO dto) {
        if (repository.existsByTenantCode(dto.getCode())) {
            throw new IllegalStateException("Tenant code already exists: " + dto.getCode());
        }
        Tenant entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
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

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public TenantDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TenantDTO getByCode(String code) {
        return repository.findByTenantCode(code)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
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
        Tenant tenant = repository.findByTenantCode(tenantCode)
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
}
