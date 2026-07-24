package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRCRAFT_MANUFACTURER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_MANUFACTURER_CODE",
                        columnNames = "MANUFACTURER_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_MANUFACTURER_NAME",
                        columnNames = "MANUFACTURER_NAME"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_MANUFACTURER_CODE",
                        columnList = "MANUFACTURER_CODE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_MANUFACTURER_NAME",
                        columnList = "MANUFACTURER_NAME"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_MANUFACTURER_COUNTRY",
                        columnList = "COUNTRY_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftManufacturer extends CSMMasterDataEntity {

    @NotBlank
    @Column(name = "MANUFACTURER_CODE", nullable = false, length = 20)
    private String manufacturerCode;

    @NotBlank
    @Column(name = "MANUFACTURER_NAME", nullable = false, length = 150)
    private String manufacturerName;

    @Column(name = "SHORT_NAME", length = 50)
    private String shortName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_MANUFACTURER_COUNTRY")
    )
    private Country country;

    @Column(name = "WEBSITE", length = 255)
    private String website;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        manufacturerCode = manufacturerCode.trim().toUpperCase();
        manufacturerName = manufacturerName.trim();

        if (shortName != null) {
            shortName = shortName.trim();
        }

        if (website != null) {
            website = website.trim();
        }
    }
}