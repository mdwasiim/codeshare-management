package com.codeshare.airline.common.utils.mapper;

import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;

public final class AuditMapperHolder {

    private static AuditMapper mapper;

    private AuditMapperHolder() {}

    public static void set(AuditMapper m) {
        mapper = m;
    }

    public static AuditMapper get() {
        return mapper;
    }
}
