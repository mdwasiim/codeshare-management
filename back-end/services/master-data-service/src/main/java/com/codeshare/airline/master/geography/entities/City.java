package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "CITY",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CITY_COUNTRY_NAME",
                        columnNames = {"COUNTRY_ID", "CITY_NAME"}
                ),
                @UniqueConstraint(
                        name = "UK_CITY_IATA",
                        columnNames = "IATA_CITY_CODE"
                )
        },
        indexes = {
                @Index(name = "IDX_CITY_COUNTRY", columnList = "COUNTRY_ID"),
                @Index(name = "IDX_CITY_REGION", columnList = "REGION_ID"),
                @Index(name = "IDX_CITY_IATA", columnList = "IATA_CITY_CODE"),
                @Index(name = "IDX_CITY_NAME", columnList = "CITY_NAME"),
                @Index(name = "IDX_CITY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class City extends CSMMasterDataEntity {

    @Column(name = "CITY_CODE", length = 20)
    private String cityCode;

    @Column(name = "CITY_NAME", nullable = false, length = 150)
    private String cityName;

    @Column(name = "IATA_CITY_CODE", length = 3)
    private String iataCityCode;

    @Column(name = "MUNICIPALITY", length = 150)
    private String municipality;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CITY_COUNTRY")
    )
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "REGION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CITY_REGION")
    )
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TIMEZONE_ID",
            foreignKey = @ForeignKey(name = "FK_CITY_TIMEZONE")
    )
    private Timezone timezone;

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (cityCode != null) {
            cityCode = cityCode.trim().toUpperCase();
        }

        if (cityName != null) {
            cityName = cityName.trim();
        }

        if (iataCityCode != null) {
            iataCityCode = iataCityCode.trim().toUpperCase();
        }

        if (municipality != null) {
            municipality = municipality.trim();
        }

    }
}
