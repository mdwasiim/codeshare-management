package com.codeshare.airline.identity.repository;

import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CSMDataBaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    Optional<User> findByUsernameAndTenant_Id(String username, UUID id);

    List<User> findAllByTenant_Id(UUID tenantId);
}