package com.codeshare.airline.common.audit;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditInfo {

    private Long id;                // Only if you want ID in DTO

    private String createdBy;
    private String updatedBy;

    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
