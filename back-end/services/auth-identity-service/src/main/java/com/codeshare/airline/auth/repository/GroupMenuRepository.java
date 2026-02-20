package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.GroupMenu;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface GroupMenuRepository extends CSMDataBaseRepository<GroupMenu, UUID> {

}