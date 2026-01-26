package com.codeshare.airline.auth.service.serviceImpl;

import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.OidcConfig;
import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.entities.OidcIdentityProviderEntity;
import com.codeshare.airline.auth.entities.OidcConfigEntity;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.TenantRepository;
import com.codeshare.airline.auth.service.TenantService;
import com.codeshare.airline.auth.utils.mappers.TenantMapper;
import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
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

    // -------------------------------------------------------------------------
    // CREATE TENANT
    // -------------------------------------------------------------------------
    @Override
    public TenantDTO create(TenantDTO dto) {

        // Unique tenant code validation
        if (repository.existsByTenantCode(dto.getCode())) {
            throw new IllegalStateException("Tenant code already exists: " + dto.getCode());
        }

        Tenant entity = mapper.toEntity(dto);
        Tenant saved = repository.save(entity);

        return mapper.toDTO(saved);
    }


    // -------------------------------------------------------------------------
    // UPDATE TENANT (partial update)
    // -------------------------------------------------------------------------
    @Override
    public TenantDTO update(UUID id, TenantDTO dto) {

        Tenant entity = repository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + id));

        // If code is changing, ensure new one is unique
        if (dto.getCode() != null && !dto.getCode().equals(entity.getTenantCode())) {
            if (repository.existsByTenantCode(dto.getCode())) {
                throw new IllegalStateException("Tenant code already exists: " + dto.getCode());
            }
        }

        // Let MapStruct apply safe, non-null updates
        mapper.updateEntityFromDto(dto, entity);

        return mapper.toDTO(repository.save(entity));
    }


    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TenantDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + id));
    }


    // -------------------------------------------------------------------------
    // GET BY CODE
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TenantDTO getByCode(String code) {
        return repository.findByTenantCode(code)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + code));
    }


    // -------------------------------------------------------------------------
    // GET ALL TENANTS
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<TenantDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }


    // -------------------------------------------------------------------------
    // DELETE TENANT
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        if (!repository.existsById(id)) {
            throw new CSMResourceNotFoundException("Tenant not found: " + id);
        }

        // ⚠️ Optional: block deletion if tenant has organizations (recommended)
        // if (organizationRepository.existsByTenantId(id)) {
        //     throw new IllegalStateException("Cannot delete tenant with organizations. Delete child entities first.");
        // }

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantContext getByTenantCode(String tenantCode) {

        if (tenantCode == null || tenantCode.isBlank()) {
            throw new IllegalArgumentException("Tenant code must be provided");
        }
        Tenant tenant = repository.findByTenantCode(tenantCode).orElseThrow(() ->
                        new AuthenticationFailedException("Invalid tenant code: " + tenantCode));

        TenantContext tenantContext = TenantContext.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .tenantCode(tenant.getTenantCode())
                .status(tenant.getStatus())
                .logoUrl(tenant.getLogoUrl())
                .region(tenant.getRegion())
                .build();

        List<IdentityProviderConfig> identityProviderConfigs =
                tenant.getIdentityProviders().stream()
                        .filter(OidcIdentityProviderEntity::isEnabled)   // ✅ only enabled
                        .sorted(Comparator.comparingInt(
                                OidcIdentityProviderEntity::getPriority)) // ✅ priority
                        .map(this::toIdentityProviderConfig)
                        .toList();

        tenantContext.setIdentityProviders(identityProviderConfigs);
        return tenantContext;
    }

    private IdentityProviderConfig toIdentityProviderConfig(OidcIdentityProviderEntity idp ) {

        IdentityProviderConfig.IdentityProviderConfigBuilder identityProviderConfigBuilder =
                IdentityProviderConfig.builder()
                        .authSource(idp.getAuthSource())
                        .enabled(idp.isEnabled())
                        .priority(idp.getPriority());

        if (idp.getOidcConfig() != null) {
            OidcConfigEntity oidcConfigEntity = idp.getOidcConfig();
            OidcConfig oidcConfig = OidcConfig.builder()
                    .issuerUri(oidcConfigEntity.getIssuerUri())
                    .authorizationUri(oidcConfigEntity.getAuthorizationUri())
                    .tokenUri(oidcConfigEntity.getTokenUri())
                    .jwkSetUri(oidcConfigEntity.getJwkSetUri())
                    .clientId(oidcConfigEntity.getClientId())
                    .clientSecretRef(oidcConfigEntity.getClientSecretRef())
                    .redirectUri(oidcConfigEntity.getRedirectUri())
                    .scopes(oidcConfigEntity.getScopes())
                    .enforceRedirectUri(oidcConfigEntity.isEnforceRedirectUri())
                    .build();
            identityProviderConfigBuilder.oidcConfig(oidcConfig);
        }
        return identityProviderConfigBuilder.build();
    }

}
