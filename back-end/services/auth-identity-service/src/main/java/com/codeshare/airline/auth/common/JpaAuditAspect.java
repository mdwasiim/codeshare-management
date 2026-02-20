package com.codeshare.airline.auth.common;


import com.codeshare.airline.core.dto.audit.context.CSMAuditContext;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAuditableEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class JpaAuditAspect {

    @AfterReturning(value = "execution(* org.springframework.data.jpa.repository.JpaRepository+.save(..))", returning = "entity")
    public void addAudit(Object entity) {

        if (!(entity instanceof CSMDataAuditableEntity base)) {
            return;
        }

        String user = CSMAuditContext.get().getUserId();
        String txn  = CSMAuditContext.get().getTransactionId();

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
