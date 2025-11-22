package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.utils.mappers.UserRoleMapper;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.repository.UserRoleRepository;
import com.codeshare.airline.auth.service.UserRoleAssignmentService;
import com.codeshare.airline.common.auth.model.UserRoleDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserRoleAssignmentServiceImpl implements UserRoleAssignmentService {

    private final UserRoleRepository repo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserRoleMapper mapper;

    public UserRoleAssignmentServiceImpl(
            UserRoleRepository repo,
            UserRepository userRepo,
            RoleRepository roleRepo,
            UserRoleMapper mapper
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mapper = mapper;
    }

    @Override
    public UserRoleDTO assignRoleToUser(UUID userId, UUID roleId) {

        if (repo.existsByUserIdAndRoleId(userId, roleId)) {
            throw new RuntimeException("Role already assigned to this user");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserRole ur = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        return mapper.toDTO(repo.save(ur));
    }

    @Override
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        repo.findByUserId(userId).stream()
                .filter(x -> x.getRole().getId().equals(roleId))
                .findFirst()
                .ifPresent(repo::delete);
    }

    @Override
    public List<UserRoleDTO> getRolesByUser(UUID userId) {
        return mapper.toDTOList(repo.findByUserId(userId));
    }

    @Override
    public List<UserRoleDTO> getUsersByRole(UUID roleId) {
        return mapper.toDTOList(repo.findByRoleId(roleId));
    }
}
