package com.codeshare.airline.data.core.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_SERVICE_TYPE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SERVICE_TYPE_CODE", columnNames = "SERVICE_TYPE_CODE")
        },
        indexes = {
                @Index(name = "IDX_SERVICE_TYPE_CODE", columnList = "SERVICE_TYPE_CODE"),
                @Index(name = "IDX_SERVICE_TYPE_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ServiceType extends CSMDataAbstractEntity {

    @Column(name = "SERVICE_TYPE_CODE", nullable = false, length = 5)
    private String serviceTypeCode;   // J, G, F, etc.

    @Column(name = "SERVICE_TYPE_NAME", nullable = false, length = 100)
    private String serviceTypeName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (serviceTypeCode != null) {
            serviceTypeCode = serviceTypeCode.toUpperCase();
        }
    }
}