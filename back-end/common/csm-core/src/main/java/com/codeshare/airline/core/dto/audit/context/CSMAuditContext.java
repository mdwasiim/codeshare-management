package com.codeshare.airline.core.dto.audit.context;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CSMAuditContext {

    private static final ThreadLocal<CSMAuditContext> CTX = ThreadLocal.withInitial(CSMAuditContext::new);

    private String userId;
    private String transactionId;

    public static CSMAuditContext get() {
        return CTX.get();
    }

    public static void clear() {
        CTX.remove();
    }
}
