package com.codeshare.airline.common.services.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AuditInterceptor {

    @AfterReturning(value = "execution(* org.springframework.data.jpa.repository.JpaRepository+.save(..))", returning = "entity")
    public void addAudit(Object entity) {

        if (!(entity instanceof AuditBaseEntity base)) {
            return;
        }

        String user = AuditContextHolder.get().getUserId();
        String txn  = AuditContextHolder.get().getTransactionId();

        LocalDateTime now = LocalDateTime.now();

        if (base.getCreatedAt() == null) {
            base.setCreatedAt(now);
            base.setCreatedBy(user);
        }

        base.setUpdatedAt(now);
        base.setUpdatedBy(user);

        base.setTransactionId(txn);
    }
}
