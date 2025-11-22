package com.codeshare.airline.tenant.serviceImpl;


import com.codeshare.airline.common.tenant.model.UserGroupDTO;
import com.codeshare.airline.tenant.entities.Organization;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.UserGroup;
import com.codeshare.airline.tenant.utils.mappers.UserGroupMapper;
import com.codeshare.airline.tenant.repository.OrganizationRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.UserGroupRepository;
import com.codeshare.airline.tenant.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final TenantRepository tenantRepository;
    private final OrganizationRepository organizationRepository;
    private final UserGroupMapper mapper;

    @Override
    public UserGroupDTO create(UserGroupDTO dto) {

        Tenant tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Organization organization = null;
        if (dto.getOrganizationId() != null) {
            organization = organizationRepository.findById(dto.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
        }

        UserGroup entity = mapper.toEntity(dto);
        entity.setTenant(tenant);
        entity.setOrganization(organization);

        return mapper.toDTO(userGroupRepository.save(entity));
    }

    @Override
    public UserGroupDTO update(UUID id, UserGroupDTO dto) {
        UserGroup entity = userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserGroup not found"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive());

        if (dto.getOrganizationId() != null) {
            entity.setOrganization(
                    organizationRepository.findById(dto.getOrganizationId())
                            .orElseThrow(() -> new RuntimeException("Organization not found"))
            );
        }

        return mapper.toDTO(userGroupRepository.save(entity));
    }

    @Override
    public UserGroupDTO getById(UUID id) {
        UserGroup entity = userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserGroup not found"));
        return mapper.toDTO(entity);
    }

    @Override
    public List<UserGroupDTO> getAll(UUID tenantId) {
        return mapper.toDTOList(
                userGroupRepository.findByTenantId(tenantId)
        );
    }

    @Override
    public void delete(UUID id) {
        UserGroup entity = userGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserGroup not found"));
        userGroupRepository.delete(entity);
    }
}
