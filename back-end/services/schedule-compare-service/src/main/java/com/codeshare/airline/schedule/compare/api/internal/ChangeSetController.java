package com.codeshare.airline.schedule.compare.api.internal;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.schedule.compare.application.ChangeSetQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping({"/schedule/internal/change-sets", "/internal/change-sets"})
@RequiredArgsConstructor
public class ChangeSetController {

    private final ChangeSetQueryService projectionService;

    @GetMapping("/{changeSetId}")
    public ChangeSetDTO getChangeSet(@PathVariable UUID changeSetId) {
        return projectionService.getChangeSet(changeSetId);
    }
}

