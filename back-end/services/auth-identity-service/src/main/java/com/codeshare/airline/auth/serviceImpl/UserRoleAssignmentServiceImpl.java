package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.entities.rbac.UserRole;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.repository.UserRoleRepository;
import com.codeshare.airline.auth.service.UserRoleAssignmentService;
import com.codeshare.airline.auth.utils.mappers.UserRoleMapper;
import com.codeshare.airline.common.auth.identity.model.UserRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleAssignmentServiceImpl implements UserRoleAssignmentService {

    private final UserRoleRepository repo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserRoleMapper mapper;


    // -------------------------------------------------------------------------
    // Assign a role to a user (User ↔ Role many-to-many mapping)
    // -------------------------------------------------------------------------
    @Override
    public UserRoleDTO assignRoleToUser(UUID userId, UUID roleId) {

        // Prevent duplicate role assignment
        if (repo.existsByUserIdAndRoleId(userId, roleId)) {
            throw new RuntimeException("Role already assigned to this user");
        }

        // Load user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Load role
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        // Create mapping
        UserRole mapping = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        return mapper.toDTO(repo.save(mapping));
    }


    // -------------------------------------------------------------------------
    // Remove role assignment from user
    // -------------------------------------------------------------------------
    @Override
    public void removeRoleFromUser(UUID userId, UUID roleId) {

        // Optimized lookup (instead of loading the entire list)
        UserRole mapping = repo.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new RuntimeException("Role not assigned to user"));

        repo.delete(mapping);
    }


    // -------------------------------------------------------------------------
    // Get all roles assigned to a user
    // -------------------------------------------------------------------------
    @Override
    public List<UserRoleDTO> getRolesByUser(UUID userId) {
        return mapper.toDTOList(repo.findByUserId(userId));
    }


    // -------------------------------------------------------------------------
    // Get all users assigned to a role
    // -------------------------------------------------------------------------
    @Override
    public List<UserRoleDTO> getUsersByRole(UUID roleId) {
        return mapper.toDTOList(repo.findByRoleId(roleId));
    }
}
