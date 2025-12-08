package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(
        name = "user_devices",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_device_user_device", columnNames = {"user_id","device_id"})
        },
        indexes = {
                @Index(name = "idx_ud_user", columnList = "user_id"),
                @Index(name = "idx_ud_device", columnList = "device_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDevice extends AbstractEntity {

    @Column(name = "device_id", length = 200, nullable = false)
    private String deviceId;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "tenant_id", columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime lastSeen;

    @Column(name = "trusted")
    private boolean trusted = true;

}
