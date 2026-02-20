package com.codeshare.airline.data.ssim.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_MESSAGE_TYPE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_MESSAGE_TYPE_CODE", columnNames = "MESSAGE_TYPE_CODE")
        },
        indexes = {
                @Index(name = "IDX_MESSAGE_TYPE_CODE", columnList = "MESSAGE_TYPE_CODE"),
                @Index(name = "IDX_MESSAGE_TYPE_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class MessageType extends CSMDataAbstractEntity {

    @Column(name = "MESSAGE_TYPE_CODE", nullable = false, length = 10)
    private String messageTypeCode;   // ASM, SSM, SCR, SHL

    @Column(name = "MESSAGE_TYPE_NAME", nullable = false, length = 100)
    private String messageTypeName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

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
        if (messageTypeCode != null) {
            messageTypeCode = messageTypeCode.toUpperCase();
        }
    }
}