package com.codeshare.airline.master.common.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "COMMON_REFERENCE_OPTION",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_COMMON_REFERENCE_OPTION", columnNames = {"CATEGORY_CODE", "OPTION_CODE"})
        },
        indexes = {
                @Index(name = "IDX_COMMON_REFERENCE_OPTION_CATEGORY", columnList = "CATEGORY_CODE"),
                @Index(name = "IDX_COMMON_REFERENCE_OPTION_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CommonReferenceOption extends CSMDataAbstractEntity {

    @Column(name = "CATEGORY_CODE", nullable = false, length = 100)
    private String categoryCode;

    @Column(name = "OPTION_CODE", nullable = false, length = 100)
    private String optionCode;

    @Column(name = "OPTION_LABEL", nullable = false, length = 200)
    private String optionLabel;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (categoryCode != null) {
            categoryCode = categoryCode.trim().toUpperCase();
        }
        if (optionCode != null) {
            optionCode = optionCode.trim().toUpperCase();
        }
        if (optionLabel != null) {
            optionLabel = optionLabel.trim();
        }
    }
}
