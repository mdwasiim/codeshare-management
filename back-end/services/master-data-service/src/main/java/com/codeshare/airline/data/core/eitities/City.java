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
        name = "MASTER_CITY",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CITY_NAME_COUNTRY",
                        columnNames = {"CITY_NAME", "COUNTRY_ID"}
                )
        },
        indexes = {
                @Index(name = "IDX_CITY_COUNTRY", columnList = "COUNTRY_ID"),
                @Index(name = "IDX_CITY_STATE", columnList = "STATE_ID"),
                @Index(name = "IDX_CITY_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class City extends CSMDataAbstractEntity {

    @Column(name = "CITY_NAME", nullable = false, length = 150)
    private String cityName;

    @Column(name = "IATA_CITY_CODE", length = 3)
    private String iataCityCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CITY_COUNTRY")
    )
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID", nullable = true,
            foreignKey = @ForeignKey(name = "FK_CITY_STATE"))
    private State state;

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
        if (iataCityCode != null) {
            iataCityCode = iataCityCode.toUpperCase();
        }
    }
}
