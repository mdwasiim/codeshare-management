package com.codeshare.airline.master.airline.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airline.entities.enums.AllianceMembershipStatus;
import com.codeshare.airline.master.airline.entities.enums.AllianceMembershipType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "ALLIANCE_MEMBER",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "UK_ALLIANCE_MEMBER",
                        columnNames = {
                                "ALLIANCE_ID",
                                "AIRLINE_ID"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_ALLIANCE_MEMBER_ALLIANCE",
                        columnList = "ALLIANCE_ID"
                ),

                @Index(
                        name = "IDX_ALLIANCE_MEMBER_AIRLINE",
                        columnList = "AIRLINE_ID"
                ),

                @Index(
                        name = "IDX_ALLIANCE_MEMBER_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_ALLIANCE_MEMBER_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AllianceMember extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ALLIANCE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ALLIANCE_MEMBER_ALLIANCE")
    )
    private Alliance alliance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ALLIANCE_MEMBER_AIRLINE")
    )
    private Airline airline;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBERSHIP_TYPE", nullable = false, length = 30)
    private AllianceMembershipType membershipType;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBERSHIP_STATUS", nullable = false, length = 30)
    private AllianceMembershipStatus membershipStatus =
            AllianceMembershipStatus.ACTIVE;

    @Column(name = "JOIN_DATE")
    private LocalDate joinDate;

    @Column(name = "LEAVE_DATE")
    private LocalDate leaveDate;

    @Column(name = "PRIMARY_MEMBER", nullable = false)
    private Boolean primaryMember = Boolean.FALSE;

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

        if (alliance == null) {
            throw new IllegalStateException("Alliance is mandatory.");
        }

        if (airline == null) {
            throw new IllegalStateException("Airline is mandatory.");
        }

        if (membershipType == null) {
            throw new IllegalStateException("Membership Type is mandatory.");
        }

        if (membershipStatus == null) {
            throw new IllegalStateException("Membership Status is mandatory.");
        }

        if (joinDate != null &&
                leaveDate != null &&
                joinDate.isAfter(leaveDate)) {

            throw new IllegalStateException(
                    "Join Date cannot be after Leave Date."
            );
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }
    }
}