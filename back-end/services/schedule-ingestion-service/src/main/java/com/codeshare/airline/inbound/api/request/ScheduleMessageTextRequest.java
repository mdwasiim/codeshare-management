package com.codeshare.airline.inbound.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleMessageTextRequest {

    @NotBlank
    private String airlineCode;

    private String fileName;

    @NotBlank
    private String content;
}
