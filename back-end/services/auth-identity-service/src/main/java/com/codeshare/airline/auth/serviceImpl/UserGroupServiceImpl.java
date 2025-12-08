package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.entities.rbac.UserGroup;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.UserGroupRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.UserGroupService;
import com.codeshare.airline.auth.utils.mappers.UserGroupMapper;
import com.codeshare.airline.common.auth.identity.model.UserGroupDTO;
import com.codeshare.airline.common.services.exceptions.BusinessException;
import com.codeshare.airline.common.services.exceptions.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final UserGroupRepository repo;
    private final UserGroupMapper mapper;


    // -------------------------------------------------------------------------
    // ASSIGN USER → GROUP
    // -------------------------------------------------------------------------
    @Override
    public UserGroupDTO assignUser(UUID userId, UUID groupId) {

        if (repo.existsByUserIdAndGroupId(userId, groupId)) {
            throw new BusinessException(
                    ErrorCodes.DUPLICATE_ENTITY,
                    "User already assigned to this group"
            );
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodes.USER_NOT_FOUND, "User not found: " + userId));

        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodes.DATA_NOT_FOUND, "Group not found: " + groupId));

        // --- Tenant safety check
        if (!user.getTenantId().equals(group.getTenantId())) {
            throw new BusinessException(
                    ErrorCodes.ACCESS_DENIED,
                    "User and Group belong to different tenants"
            );
        }

        UserGroup ug = UserGroup.builder()
                .user(user)
                .group(group)
                .build();

        UserGroup saved = repo.save(ug);

        log.info("✔ User {} assigned → Group {} (Tenant: {})",
                user.getUsername(), group.getCode(), user.getTenantId());

        return mapper.toDTO(saved);
    }


    // -------------------------------------------------------------------------
    // REMOVE USER → GROUP
    // -------------------------------------------------------------------------
    @Override
    public void removeUser(UUID userId, UUID groupId) {

        UserGroup ug = repo.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodes.DATA_NOT_FOUND,
                        "User is not assigned to this group"
                ));

        repo.delete(ug);  // soft delete from AbstractEntity

        log.info("✔ User {} removed ← Group {}", userId, groupId);
    }


    // -------------------------------------------------------------------------
    // LIST GROUPS OF A USER
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<UserGroupDTO> getGroupsByUser(UUID userId) {

        List<UserGroup> list = repo.findByUserId(userId);

        log.info("Fetched {} groups for user {}", list.size(), userId);

        return mapper.toDTOList(list);
    }


    // -------------------------------------------------------------------------
    // LIST USERS IN A GROUP
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<UserGroupDTO> getUsersByGroup(UUID groupId) {

        List<UserGroup> list = repo.findByGroupId(groupId);

        log.info("Fetched {} users for group {}", list.size(), groupId);

        return mapper.toDTOList(list);
    }


    // -------------------------------------------------------------------------
    // TENANT SERVICE SYNC (group comes from tenant microservice)
    // -------------------------------------------------------------------------
    @Override
    public void assignUserViaSync(UUID tenantGroupId, UUID userId) {

        Group group = groupRepo.findByTenantGroupId(tenantGroupId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodes.DATA_NOT_FOUND,
                        "Mapped group not found for tenantGroupId: " + tenantGroupId
                ));

        assignUser(userId, group.getId());
    }


    @Override
    public void removeUserViaSync(UUID tenantGroupId, UUID userId) {

        Group group = groupRepo.findByTenantGroupId(tenantGroupId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodes.DATA_NOT_FOUND,
                        "Mapped group not found for tenantGroupId: " + tenantGroupId
                ));

        removeUser(userId, group.getId());
    }
}
