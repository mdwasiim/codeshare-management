package com.codeshare.airline.master.airline.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airline.entities.AllianceMember;

import java.util.UUID;

public interface AllianceMemberRepository extends CSMDataBaseRepository<AllianceMember, UUID> {
}
