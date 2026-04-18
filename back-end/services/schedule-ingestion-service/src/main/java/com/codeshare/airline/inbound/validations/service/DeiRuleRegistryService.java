package com.codeshare.airline.inbound.validations.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DeiRuleRegistryService {

    private final Map<Integer, DeiRule> rules = new HashMap<>();

    public DeiRuleRegistryService(List<DeiRule> ruleList) {
        for (DeiRule rule : ruleList) {
            rules.put(rule.getCode(), rule);
        }
    }

    public Optional<DeiRule> getRule(int code) {
        return Optional.ofNullable(rules.get(code));
    }
}