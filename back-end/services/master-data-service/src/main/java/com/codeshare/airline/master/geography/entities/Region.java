package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "REGION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_REGION_COUNTRY_CODE",
                        columnNames = {"COUNTRY_ID", "REGION_CODE"}
                )
        },
        indexes = {
                @Index(name = "IDX_REGION_COUNTRY", columnList = "COUNTRY_ID"),
                @Index(name = "IDX_REGION_CODE", columnList = "REGION_CODE"),
                @Index(name = "IDX_REGION_NAME", columnList = "REGION_NAME"),
                @Index(name = "IDX_REGION_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Region extends CSMMasterDataEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_REGION_COUNTRY")
    )
    private Country country;

    @Column(name = "REGION_CODE", nullable = false, length = 20)
    private String regionCode;

    @Column(name = "REGION_NAME", nullable = false, length = 150)
    private String regionName;

    @Column(name = "REGION_TYPE", length = 50)
    private String regionType;

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (regionCode != null) {
            regionCode = regionCode.trim().toUpperCase();
        }

        if (regionName != null) {
            regionName = regionName.trim();
        }

        if (regionType != null) {
            regionType = regionType.trim();
        }
    }
}

