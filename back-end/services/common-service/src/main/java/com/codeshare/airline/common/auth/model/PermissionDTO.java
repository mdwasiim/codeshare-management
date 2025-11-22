package com.codeshare.airline.common.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {

    private UUID id;
    private String name;
    private String code;
    private String description;

}