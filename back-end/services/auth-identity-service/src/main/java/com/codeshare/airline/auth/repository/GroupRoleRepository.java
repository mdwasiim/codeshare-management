package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.GroupRole;
import com.codeshare.airline.auth.entities.Group;
import com.codeshare.airline.auth.entities.Role;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRoleRepository extends CSMDataBaseRepository<GroupRole, UUID> {

    List<GroupRole> findByGroup_Id(UUID groupId);

    List<GroupRole> findByRole_Id(UUID groupId);

    boolean existsByGroup_IdAndRole_Id(UUID groupId, UUID roleId);

    boolean existsByTenantAndGroupAndRole(Tenant tenant, Group group, Role role);
}
