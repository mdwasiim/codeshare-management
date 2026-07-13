package com.codeshare.airline.schedule.live.api.controller;

import com.codeshare.airline.schedule.live.application.ScheduleChangeRequestService;
import com.codeshare.airline.schedule.live.domain.entity.ScheduleChangeRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule/change-requests")
@RequiredArgsConstructor
public class ScheduleChangeRequestController {

    private final ScheduleChangeRequestService scheduleChangeRequestService;

    @GetMapping("/{changeSetId}")
    public ScheduleChangeRequestEntity getChangeRequest(@PathVariable UUID changeSetId) {
        return scheduleChangeRequestService.getByChangeSetId(changeSetId);
    }

    @PostMapping("/{changeSetId}/approve")
    public ScheduleChangeRequestEntity approve(
            @PathVariable UUID changeSetId,
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        return scheduleChangeRequestService.approve(changeSetId, userId);
    }
}
