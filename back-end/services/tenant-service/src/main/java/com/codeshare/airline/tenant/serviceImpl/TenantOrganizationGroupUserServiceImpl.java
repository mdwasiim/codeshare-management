package com.codeshare.airline.tenant.serviceImpl;

import com.codeshare.airline.common.auth.identity.model.TenantGroupUserSyncDTO;
import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupUserDTO;
import com.codeshare.airline.tenant.entities.TenantOrganizationGroupUser;
import com.codeshare.airline.tenant.feign.client.AuthGroupSyncClient;
import com.codeshare.airline.tenant.repository.TenantOrganizationGroupUserRepository;
import com.codeshare.airline.tenant.service.TenantOrganizationGroupUserService;
import com.codeshare.airline.tenant.utils.mappers.TenantOrganizationGroupUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantOrganizationGroupUserServiceImpl implements TenantOrganizationGroupUserService {

    private final TenantOrganizationGroupUserRepository repo;
    private final TenantOrganizationGroupUserMapper mapper;
    private final AuthGroupSyncClient authGroupSyncClient;


    // -------------------------------------------------------------------------
    // ASSIGN USER TO GROUP + SYNC TO IDENTITY-SERVICE
    // -------------------------------------------------------------------------
    @Override
    public TenantOrganizationGroupUserDTO assignUserToGroup(TenantOrganizationGroupUserDTO dto) {

        // Prevent duplicates
        if (repo.existsByGroupIdAndUserId(dto.getGroupId(), dto.getUserId())) {
            throw new IllegalStateException("User already assigned to this group");
        }

        TenantOrganizationGroupUser saved = repo.save(mapper.toEntity(dto));
        TenantOrganizationGroupUserDTO response = mapper.toDTO(saved);

        // 🔄 Sync to identity-service RBAC UserGroup
        authGroupSyncClient.assignUser(
                TenantGroupUserSyncDTO.builder()
                        .tenantGroupId(response.getGroupId())
                        .userId(response.getUserId())
                        .build()
        );

        return response;
    }


    // -------------------------------------------------------------------------
    // REMOVE USER FROM GROUP + SYNC
    // -------------------------------------------------------------------------
    @Override
    public void removeUserFromGroup(UUID groupId, UUID userId) {

        // Optimized DB lookup instead of filtering in memory
        TenantOrganizationGroupUser entity = repo.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not assigned to this group"
                ));

        repo.delete(entity);

        // 🔄 Sync removal to identity-service
        authGroupSyncClient.removeUser(
                TenantGroupUserSyncDTO.builder()
                        .tenantGroupId(groupId)
                        .userId(userId)
                        .build()
        );
    }


    // -------------------------------------------------------------------------
    // GET ALL USERS IN A GROUP
    // -------------------------------------------------------------------------
    @Override
    public List<TenantOrganizationGroupUserDTO> getUsersByGroup(UUID groupId) {
        return mapper.toDTOList(repo.findByGroupId(groupId));
    }


    // -------------------------------------------------------------------------
    // GET ALL GROUPS A USER BELONGS TO
    // -------------------------------------------------------------------------
    @Override
    public List<TenantOrganizationGroupUserDTO> getGroupsByUser(UUID userId) {
        return mapper.toDTOList(repo.findByUserId(userId));
    }
}
