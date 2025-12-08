package com.codeshare.airline.common.services.audit;


import lombok.Data;

@Data
public class AuditContextHolder {

    private static final ThreadLocal<AuditContextHolder> CTX = ThreadLocal.withInitial(AuditContextHolder::new);

    private String userId;
    private String transactionId;

    public static AuditContextHolder get() {
        return CTX.get();
    }

    public static void clear() {
        CTX.remove();
    }
}
