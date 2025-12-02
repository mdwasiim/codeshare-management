package com.codeshare.airline.common.utils.mapper;

import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import org.springframework.stereotype.Component;

@Component
public class AuditMapperInitializer {
    public AuditMapperInitializer(AuditMapper mapper) {
        AuditMapperHolder.set(mapper);
    }
}
