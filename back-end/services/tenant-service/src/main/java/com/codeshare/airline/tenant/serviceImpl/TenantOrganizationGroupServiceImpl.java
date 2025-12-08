package com.codeshare.airline.tenant.serviceImpl;

import com.codeshare.airline.common.auth.identity.model.TenantGroupSyncDTO;
import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupDTO;
import com.codeshare.airline.tenant.entities.TenantOrganizationGroup;
import com.codeshare.airline.tenant.feign.client.AuthGroupSyncClient;
import com.codeshare.airline.tenant.repository.TenantOrganizationGroupRepository;
import com.codeshare.airline.tenant.service.TenantOrganizationGroupService;
import com.codeshare.airline.tenant.utils.mappers.TenantOrganizationGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantOrganizationGroupServiceImpl implements TenantOrganizationGroupService {

    private final TenantOrganizationGroupRepository repo;
    private final TenantOrganizationGroupMapper mapper;
    private final AuthGroupSyncClient authGroupSyncClient;


    // -------------------------------------------------------------------------
    // CREATE NEW TENANT ORGANIZATION GROUP
    // -------------------------------------------------------------------------
    @Override
    public TenantOrganizationGroupDTO create(TenantOrganizationGroupDTO dto) {

        TenantOrganizationGroup saved = repo.save(mapper.toEntity(dto));
        TenantOrganizationGroupDTO response = mapper.toDTO(saved);

        // 🔄 Sync to auth-service (RBAC group creation)
        authGroupSyncClient.syncGroup(
                TenantGroupSyncDTO.builder()
                        .tenantGroupId(response.getId())
                        .tenantId(response.getTenantId())
                        .organizationId(response.getOrganizationId())
                        .code(response.getCode())
                        .name(response.getName())
                        .description(response.getDescription())
                        .build()
        );

        return response;
    }


    // -------------------------------------------------------------------------
    // UPDATE EXISTING GROUP + SYNC
    // -------------------------------------------------------------------------
    @Override
    public TenantOrganizationGroupDTO update(UUID id, TenantOrganizationGroupDTO dto) {

        TenantOrganizationGroup entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + id));

        // Apply only non-null fields
        mapper.updateEntityFromDto(dto, entity);

        TenantOrganizationGroup updated = repo.save(entity);
        TenantOrganizationGroupDTO result = mapper.toDTO(updated);

        // 🔄 Sync updated values to identity-service
        authGroupSyncClient.syncGroup(
                TenantGroupSyncDTO.builder()
                        .tenantGroupId(id)
                        .tenantId(result.getTenantId())
                        .organizationId(result.getOrganizationId())
                        .code(result.getCode())
                        .name(result.getName())
                        .description(result.getDescription())
                        .build()
        );

        return result;
    }


    // -------------------------------------------------------------------------
    // DELETE GROUP + SYNC REMOVAL
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Group not found: " + id);
        }

        repo.deleteById(id);

        // 🔄 Notify identity-service to remove RBAC group
        authGroupSyncClient.deleteGroup(id);
    }


    // -------------------------------------------------------------------------
    // FETCH GROUP BY ID
    // -------------------------------------------------------------------------
    @Override
    public TenantOrganizationGroupDTO getById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + id));
    }


    // -------------------------------------------------------------------------
    // FETCH GROUPS FOR A TENANT
    // -------------------------------------------------------------------------
    @Override
    public List<TenantOrganizationGroupDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(repo.findByTenantId(tenantId));
    }


    // -------------------------------------------------------------------------
    // FETCH GROUPS FOR AN ORGANIZATION
    // -------------------------------------------------------------------------
    @Override
    public List<TenantOrganizationGroupDTO> getByOrganization(UUID organizationId) {
        return mapper.toDTOList(repo.findByOrganizationId(organizationId)); // ✅ Corrected
    }
}
