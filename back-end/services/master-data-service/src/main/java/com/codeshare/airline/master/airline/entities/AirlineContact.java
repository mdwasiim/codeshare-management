package com.codeshare.airline.master.airline.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airline.entities.enums.AirlineContactType;
import com.codeshare.airline.master.airline.entities.enums.CommunicationMethod;
import com.codeshare.airline.master.georegion.eitities.Timezone;
import com.codeshare.airline.master.reference.entities.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRLINE_CONTACT",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_CONTACT",
                        columnNames = {
                                "AIRLINE_ID",
                                "CONTACT_CODE"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_AIRLINE_CONTACT_AIRLINE",
                        columnList = "AIRLINE_ID"
                ),

                @Index(
                        name = "IDX_AIRLINE_CONTACT_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_AIRLINE_CONTACT_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineContact extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRLINE_CONTACT_AIRLINE")
    )
    private AirlineCarrier airline;

    /**
     * Business Key
     */
    @Column(name = "CONTACT_CODE", nullable = false, length = 30)
    private String contactCode;

    /**
     * Person Name
     */
    @Column(name = "CONTACT_NAME", nullable = false, length = 150)
    private String contactName;

    /**
     * Job Title
     * Example:
     * Manager
     * Director
     * Coordinator
     */
    @Column(name = "DESIGNATION", length = 100)
    private String designation;

    /**
     * Department
     * Example:
     * Codeshare
     * Scheduling
     * Operations
     */
    @Column(name = "DEPARTMENT", length = 100)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTACT_TYPE", nullable = false, length = 40)
    private AirlineContactType contactType;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "PHONE", length = 30)
    private String phone;

    @Column(name = "MOBILE", length = 30)
    private String mobile;

    @Column(name = "FAX", length = 30)
    private String fax;

    @Enumerated(EnumType.STRING)
    @Column(name = "PREFERRED_COMMUNICATION", length = 30)
    private CommunicationMethod preferredCommunication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "LANGUAGE_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_CONTACT_LANGUAGE")
    )
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TIMEZONE_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_CONTACT_TIMEZONE")
    )
    private Timezone timeZone;

    @Column(name = "AVAILABLE_24X7", nullable = false)
    private Boolean available24x7 = Boolean.FALSE;

    @Column(name = "EMERGENCY_CONTACT", nullable = false)
    private Boolean emergencyContact = Boolean.FALSE;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validateAndNormalize() {

        if (airline == null) {
            throw new IllegalStateException("Airline is mandatory.");
        }

        if (contactCode == null || contactCode.isBlank()) {
            throw new IllegalStateException("Contact Code is mandatory.");
        }

        if (contactName == null || contactName.isBlank()) {
            throw new IllegalStateException("Contact Name is mandatory.");
        }

        if (contactType == null) {
            throw new IllegalStateException("Contact Type is mandatory.");
        }

        contactCode = contactCode.trim().toUpperCase();
        contactName = contactName.trim();

        if (designation != null) {
            designation = designation.trim();
        }

        if (department != null) {
            department = department.trim();
        }

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (phone != null) {
            phone = phone.trim();
        }

        if (mobile != null) {
            mobile = mobile.trim();
        }

        if (fax != null) {
            fax = fax.trim();
        }

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}
