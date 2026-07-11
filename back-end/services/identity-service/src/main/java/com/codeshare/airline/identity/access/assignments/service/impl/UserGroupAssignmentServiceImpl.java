package com.codeshare.airline.identity.access.assignments.service.impl;

import com.codeshare.airline.core.dto.tenant.GroupDTO;
import com.codeshare.airline.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.assignments.repository.UserGroupRepository;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import com.codeshare.airline.identity.access.assignments.service.UserGroupAssignmentService;
import com.codeshare.airline.identity.access.identity.mappers.GroupMapper;
import com.codeshare.airline.identity.access.assignments.mappers.UserGroupMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGroupAssignmentServiceImpl
        implements UserGroupAssignmentService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final UserGroupRepository userGroupRepository;

    private final GroupMapper groupMapper;

    private final UserGroupMapper userGroupMapper;

    // =====================================================
    // GET GROUPS BY USER
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> getGroupsByUser(UUID userId) {

        log.info("Fetching groups for userId: {}", userId);

        List<UserGroup> userGroups = userGroupRepository.findByUser_Id(userId);

        List<GroupDTO> groups = userGroups.stream()
                .map(UserGroup::getGroup)
                .map(groupMapper::toDTO)
                .toList();

        log.info("Found {} groups for userId: {}", groups.size(), userId);

        return groups;
    }

    // =====================================================
    // REPLACE USER GROUPS
    // =====================================================
    @Transactional
    @Override
    public List<UserGroupDTO> replaceUserGroups(
            UUID userId,
            List<UUID> groupIds
    ) {

        log.info("Replacing groups for userId: {}", userId);

        UUID tenantId = TenantContextHolder.getTenant().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userId)
                );

        if (!Objects.equals(user.getTenantId(), tenantId)) {
            throw new RuntimeException("User does not belong to current tenant: " + userId);
        }

        // Remove duplicates from request
        Set<UUID> uniqueGroupIds = new HashSet<>(groupIds == null ? List.of() : groupIds);

        // Delete existing mappings
        userGroupRepository.deleteByUser_Id(userId);

        // Flush immediately
        userGroupRepository.flush();

        // Create new mappings
        List<UserGroup> userGroups = uniqueGroupIds.stream()
                .map(groupId -> {

                    Group group = groupRepository.findById(groupId)
                            .orElseThrow(() ->
                                    new RuntimeException("Group not found: " + groupId)
                            );

                    if (!Objects.equals(group.getTenantId(), tenantId)) {
                        throw new RuntimeException("Group does not belong to current tenant: " + groupId);
                    }

                    return UserGroup.builder()
                            .tenantId(tenantId)
                            .user(user)
                            .group(group)
                            .build();
                })
                .collect(Collectors.toList());

        userGroupRepository.saveAll(userGroups);

        log.info("Assigned {} groups to userId: {}",
                userGroups.size(),
                userId
        );

        return userGroupMapper.toDTOList(userGroups);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<String> resolveGroupCodes(
            UUID userId
    ) {

        return userGroupRepository
                .findByUserIdWithGroup(userId)
                .stream()

                .map(UserGroup::getGroup)

                .filter(Objects::nonNull)

                .map(Group::getCode)

                .collect(Collectors.toSet());
    }
}
