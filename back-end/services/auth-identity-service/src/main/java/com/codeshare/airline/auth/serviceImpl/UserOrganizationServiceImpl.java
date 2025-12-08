package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.UserOrganization;
import com.codeshare.airline.auth.repository.UserOrganizationRepository;
import com.codeshare.airline.auth.service.UserOrganizationService;
import com.codeshare.airline.auth.utils.mappers.UserOrganizationMapper;
import com.codeshare.airline.common.auth.identity.model.UserOrganizationDTO;
import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOrganizationServiceImpl implements UserOrganizationService {

    private final UserOrganizationRepository repository;
    private final UserOrganizationMapper mapper;


    // ------------------------------------------------------------------------
    // Assign user to an organization
    // ------------------------------------------------------------------------
    @Override
    public UserOrganizationDTO assignUserToOrganization(UserOrganizationDTO dto) {

        if (repository.existsByUser_IdAndOrganizationId(dto.getUserId(), dto.getOrganizationId())) {
            throw new IllegalStateException("User already assigned to this organization");
        }

        UserOrganization entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }


    // ------------------------------------------------------------------------
    // Get organizations a user belongs to
    // ------------------------------------------------------------------------
    @Override
    public List<UserOrganizationDTO> getOrganizationsByUser(UUID userId) {
        List<UserOrganization> mappings = repository.getByUserId(userId);
        return mapper.toDTOList(mappings);
    }


    // ------------------------------------------------------------------------
    // Get all users assigned to an organization
    // ------------------------------------------------------------------------
    @Override
    public List<UserOrganizationDTO> getUsersByOrganization(UUID orgId) {
        return repository.findByOrganizationId(orgId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


    // ------------------------------------------------------------------------
    // Set primary organization for a user
    // ------------------------------------------------------------------------
    @Override
    @Transactional
    public UserOrganizationDTO setPrimary(UUID mappingId) {

        // 1️⃣ Load selected mapping
        UserOrganization selected = repository.findById(mappingId)
                .orElseThrow(() -> new ResourceNotFoundException("Mapping not found: " + mappingId));

        UUID userId = selected.getUser().getId();

        // 2️⃣ Find current primary (if exists)
        UserOrganization existingPrimary = repository.findByUserIdAndPrimaryTrue(userId);

        // 3️⃣ Reset old primary if exists
        if (existingPrimary != null && !existingPrimary.getId().equals(mappingId)) {
            existingPrimary.setPrimary(false);
            repository.save(existingPrimary);
        }

        // 4️⃣ Mark selected mapping as primary
        selected.setPrimary(true);
        return mapper.toDTO(repository.save(selected));
    }


    // ------------------------------------------------------------------------
    // Remove mapping by ID
    // ------------------------------------------------------------------------
    @Override
    public void removeUserFromOrganization(UUID mappingId) {
        if (!repository.existsById(mappingId)) {
            throw new ResourceNotFoundException("Mapping not found: " + mappingId);
        }
        repository.deleteById(mappingId);
    }
}
