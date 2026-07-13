package com.codeshare.airline.schedule.live.application;

import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScheduleApprovalPolicy {

    private final ApprovalMode approvalMode;

    public ScheduleApprovalPolicy(
            @Value("${schedule.workflow.approval.policy:${schedule.workflow.approval.mode:AUTO}}") String configuredPolicy
    ) {
        this.approvalMode = ApprovalMode.valueOf(configuredPolicy.trim().toUpperCase());
    }

    public ApprovalMode approvalMode() {
        return approvalMode;
    }

    public boolean isAutoApproval() {
        return approvalMode == ApprovalMode.AUTO;
    }

    public ApprovalStatus initialStatus() {
        return isAutoApproval() ? ApprovalStatus.APPROVED : ApprovalStatus.PENDING_APPROVAL;
    }
}
