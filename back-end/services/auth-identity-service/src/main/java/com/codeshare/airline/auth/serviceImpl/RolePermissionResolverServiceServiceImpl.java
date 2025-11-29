package com.codeshare.airline.auth.serviceImpl;


import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.auth.entities.authorization.UserGroupRole;
import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.repository.*;
import com.codeshare.airline.auth.service.RolePermissionResolverService;
import com.codeshare.airline.auth.utils.mappers.PermissionMapper;
import com.codeshare.airline.auth.utils.mappers.RoleMapper;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.auth.model.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RolePermissionResolverServiceServiceImpl implements RolePermissionResolverService {


    private final GroupRoleRepository groupRoleRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final PermissionRepository permissionRepository;

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public RolePermissionResolverServiceServiceImpl(GroupRoleRepository groupRoleRepository, RoleRepository roleRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, UserGroupRoleRepository userGroupRoleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.groupRoleRepository = groupRoleRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userGroupRoleRepository = userGroupRoleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }


    @Override
    public Set<RoleDTO> resolveRoleNames(UUID userId, UUID tenantId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Set<Role> roles = new HashSet<>();

        // 1️⃣ Direct User Roles
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        roles.addAll(userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet()));

        // 2️⃣ Group → GroupRole mapping
        List<UserGroupRole> ugrList = userGroupRoleRepository.findByUserId(userId);
        for (UserGroupRole ugr : ugrList) {
            UUID groupId = ugr.getGroup().getId();

            List<GroupRole> groupRoles =
                    groupRoleRepository.findByGroup_Id(groupId);

            roles.addAll(groupRoles.stream()
                    .map(gr -> gr.getRole())
                    .collect(Collectors.toSet()));
        }

        // 4️⃣ Return role names
        return roleMapper.toDTOSet(roles);
    }

    @Override
    public Set<PermissionDTO> resolvePermissionsNames(UUID userId, UUID tenantId) {
        Set<UUID> roleIds = new HashSet<>();

        Set<RoleDTO> roleDTOS = resolveRoleNames(userId, tenantId);

        if(!CollectionUtils.isEmpty(roleDTOS)){
            roleIds = roleDTOS.stream().map(RoleDTO::getId).collect(Collectors.toSet());
        }

        Set<Permission> permissionByIds = new HashSet<>(permissionRepository.findAllById(roleIds));
        return permissionMapper.toDTOSet(permissionByIds);
    }
}

