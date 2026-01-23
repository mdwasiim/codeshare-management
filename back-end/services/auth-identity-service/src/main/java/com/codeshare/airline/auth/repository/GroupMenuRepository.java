package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.model.entities.GroupMenu;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface GroupMenuRepository extends CSMDataBaseRepository<GroupMenu, UUID> {

}