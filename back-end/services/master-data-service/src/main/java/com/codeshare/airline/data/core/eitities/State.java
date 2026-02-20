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
        name = "MASTER_STATE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_STATE_CODE_COUNTRY",
                        columnNames = {"STATE_CODE", "COUNTRY_ID"}
                )
        },
        indexes = {
                @Index(name = "IDX_STATE_COUNTRY", columnList = "COUNTRY_ID"),
                @Index(name = "IDX_STATE_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class State extends CSMDataAbstractEntity {

    @Column(name = "STATE_CODE", nullable = false, length = 10)
    private String stateCode;

    @Column(name = "STATE_NAME", nullable = false, length = 150)
    private String stateName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_STATE_COUNTRY")
    )
    private Country country;

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
        if (stateCode != null) {
            stateCode = stateCode.toUpperCase();
        }
    }
}
