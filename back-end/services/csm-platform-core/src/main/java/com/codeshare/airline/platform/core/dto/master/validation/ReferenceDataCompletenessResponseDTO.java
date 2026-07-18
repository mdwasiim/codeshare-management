package com.codeshare.airline.platform.core.dto.master.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceDataCompletenessResponseDTO {

    private boolean complete;
    private List<ReferenceDataCompletenessIssueDTO> issues = new ArrayList<>();
}
