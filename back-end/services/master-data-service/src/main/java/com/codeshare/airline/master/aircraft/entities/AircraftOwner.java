package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftOwnerType;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRCRAFT_OWNER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_OWNER_CODE",
                        columnNames = "OWNER_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_OWNER_NAME",
                        columnNames = "OWNER_NAME"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_CODE",
                        columnList = "OWNER_CODE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_NAME",
                        columnList = "OWNER_NAME"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_TYPE",
                        columnList = "OWNER_TYPE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_COUNTRY",
                        columnList = "COUNTRY_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftOwner extends CSMMasterDataEntity {

    @NotBlank
    @Column(name = "OWNER_CODE", nullable = false, length = 20)
    private String ownerCode;

    @NotBlank
    @Column(name = "OWNER_NAME", nullable = false, length = 150)
    private String ownerName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "OWNER_TYPE", nullable = false, length = 30)
    private AircraftOwnerType ownerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_AIRCRAFT_OWNER_COUNTRY")
    )
    private Country country;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        ownerCode = ownerCode.trim().toUpperCase();
        ownerName = ownerName.trim();
    }
}