package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AllianceMember;

import java.util.UUID;

public interface AllianceMemberRepository extends CSMDataBaseRepository<AllianceMember, UUID> {
}
