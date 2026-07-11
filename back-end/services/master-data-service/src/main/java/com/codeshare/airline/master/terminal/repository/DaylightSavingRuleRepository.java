package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.DaylightSavingRule;

import java.util.Optional;
import java.util.UUID;

public interface DaylightSavingRuleRepository extends CSMDataBaseRepository<DaylightSavingRule, UUID> {

    Optional<DaylightSavingRule> findByRuleCode(String ruleCode);

    boolean existsByRuleCode(String ruleCode);
}
