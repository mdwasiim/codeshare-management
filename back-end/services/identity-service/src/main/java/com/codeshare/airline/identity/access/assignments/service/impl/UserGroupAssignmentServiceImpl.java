package com.codeshare.airline.identity.access.assignments.service.impl;

import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.platform.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.assignments.repository.UserGroupRepository;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import com.codeshare.airline.identity.access.assignments.service.UserGroupAssignmentService;
import com.codeshare.airline.identity.access.identity.mappers.GroupMapper;
import com.codeshare.airline.identity.access.identity.mappers.UserMapper;
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

    private final UserMapper userMapper;

    private final UserGroupMapper userGroupMapper;

    // =====================================================
    // GET GROUPS BY USER
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> getGroupsByUser(Long userId) {

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
    // GET USERS BY GROUP
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<AuthUserDTO> getUsersByGroup(Long groupId) {

        log.info("Fetching users for groupId: {}", groupId);

        Long tenantId = TenantContextHolder.getTenant().getId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found: " + groupId));

        if (!Objects.equals(group.getTenantId(), tenantId)) {
            throw new RuntimeException("Group does not belong to current tenant: " + groupId);
        }

        List<AuthUserDTO> users = userGroupRepository.findByGroup_Id(groupId).stream()
                .map(UserGroup::getUser)
                .map(userMapper::toDTO)
                .toList();

        log.info("Found {} users for groupId: {}", users.size(), groupId);

        return users;
    }

    // =====================================================
    // REPLACE USER GROUPS
    // =====================================================
    @Transactional
    @Override
    public List<UserGroupDTO> replaceUserGroups(
            Long userId,
            List<Long> groupIds
    ) {

        log.info("Replacing groups for userId: {}", userId);

        Long tenantId = TenantContextHolder.getTenant().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userId)
                );

        if (!Objects.equals(user.getTenantId(), tenantId)) {
            throw new RuntimeException("User does not belong to current tenant: " + userId);
        }

        // Remove duplicates from request
        Set<Long> uniqueGroupIds = new HashSet<>(groupIds == null ? List.of() : groupIds);

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

    // =====================================================
    // REPLACE GROUP USERS
    // =====================================================
    @Transactional
    @Override
    public List<UserGroupDTO> replaceGroupUsers(
            Long groupId,
            List<Long> userIds
    ) {

        log.info("Replacing users for groupId: {}", groupId);

        Long tenantId = TenantContextHolder.getTenant().getId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found: " + groupId));

        if (!Objects.equals(group.getTenantId(), tenantId)) {
            throw new RuntimeException("Group does not belong to current tenant: " + groupId);
        }

        Set<Long> uniqueUserIds = new HashSet<>(userIds == null ? List.of() : userIds);

        userGroupRepository.deleteByGroup_Id(groupId);
        userGroupRepository.flush();

        if (uniqueUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserGroup> userGroups = uniqueUserIds.stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));

                    if (!Objects.equals(user.getTenantId(), tenantId)) {
                        throw new RuntimeException("User does not belong to current tenant: " + userId);
                    }

                    return UserGroup.builder()
                            .tenantId(tenantId)
                            .user(user)
                            .group(group)
                            .build();
                })
                .collect(Collectors.toList());

        List<UserGroup> saved = userGroupRepository.saveAll(userGroups);

        log.info("Assigned {} users to groupId: {}", saved.size(), groupId);

        return userGroupMapper.toDTOList(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<String> resolveGroupCodes(
            Long userId
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
