package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "COUNTRY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_COUNTRY_ISO2", columnNames = "ISO2_CODE"),
                @UniqueConstraint(name = "UK_COUNTRY_ISO3", columnNames = "ISO3_CODE")
        },
        indexes = {
                @Index(name = "IDX_COUNTRY_ISO2", columnList = "ISO2_CODE"),
                @Index(name = "IDX_COUNTRY_ISO3", columnList = "ISO3_CODE"),
                @Index(name = "IDX_COUNTRY_NAME", columnList = "COUNTRY_NAME"),
                @Index(name = "IDX_COUNTRY_CONTINENT", columnList = "CONTINENT_CODE"),
                @Index(name = "IDX_COUNTRY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Country extends CSMMasterDataEntity {

    @Column(name = "ISO2_CODE", nullable = false, length = 2)
    private String iso2Code;

    @Column(name = "ISO3_CODE", nullable = false, length = 3)
    private String iso3Code;

    @Column(name = "NUMERIC_CODE", length = 3)
    private String numericCode;

    @Column(name = "COUNTRY_NAME", nullable = false, length = 150)
    private String countryName;

    @Column(name = "OFFICIAL_NAME", length = 250)
    private String officialName;

    @Column(name = "NATIONALITY", length = 100)
    private String nationality;

    @Column(name = "CONTINENT_CODE", nullable = false, length = 5)
    private String continentCode;

    @Column(name = "DIAL_CODE", length = 10)
    private String dialCode;

    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;

    @Column(name = "DEFAULT_TIMEZONE", length = 100)
    private String defaultTimezone;

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (iso2Code != null) {
            iso2Code = iso2Code.trim().toUpperCase();
        }

        if (iso3Code != null) {
            iso3Code = iso3Code.trim().toUpperCase();
        }

        if (continentCode != null) {
            continentCode = continentCode.trim().toUpperCase();
        }

        if (currencyCode != null) {
            currencyCode = currencyCode.trim().toUpperCase();
        }

        if (countryName != null) {
            countryName = countryName.trim();
        }

        if (officialName != null) {
            officialName = officialName.trim();
        }

        if (nationality != null) {
            nationality = nationality.trim();
        }

        if (defaultTimezone != null) {
            defaultTimezone = defaultTimezone.trim();
        }
    }
}

