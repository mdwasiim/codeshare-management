package com.codeshare.airline.data.messaging.eitities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.schedule.DeiFunctionType;
import com.codeshare.airline.core.enums.schedule.DeiScopeLevel;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "MASTER_DEI",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_DEI_NUMBER",
                        columnNames = "DEI_NUMBER"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeiRegistry extends CSMDataAbstractEntity {

    @Column(name = "DEI_NUMBER", nullable = false, length = 3)
    private String deiNumber;

    @Column(name = "DEI_NAME", nullable = false, length = 200)
    private String deiName;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "SCOPE_LEVEL", nullable = false, length = 30)
    private DeiScopeLevel scopeLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "FUNCTION_TYPE", nullable = false, length = 30)
    private DeiFunctionType functionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;


    public DeiRegistry(String deiNumber,
                       String deiName,
                       DeiScopeLevel scopeLevel,
                       DeiFunctionType functionType,
                       RecordStatus recordStatus) {

        this.deiNumber = deiNumber;
        this.deiName = deiName;
        this.scopeLevel = scopeLevel;
        this.functionType = functionType;
        this.recordStatus = recordStatus;
    }

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (deiNumber != null) {
            deiNumber = deiNumber.trim();
        }

        if (!deiNumber.matches("\\d{3}")) {
            throw new IllegalStateException(
                    "DEI number must be exactly 3 digits."
            );
        }

        if (deiName != null) {
            deiName = deiName.trim();
        }
    }
}
