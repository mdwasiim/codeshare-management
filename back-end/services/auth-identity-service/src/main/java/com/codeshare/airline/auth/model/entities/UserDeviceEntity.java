package com.codeshare.airline.auth.model.entities;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_devices",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_device_user_device",
                        columnNames = {"user_id", "device_id"}
                )
        },
        indexes = {
                @Index(name = "idx_ud_user", columnList = "user_id"),
                @Index(name = "idx_ud_device", columnList = "device_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class UserDeviceEntity extends CSMDataAbstractEntity {

    @Column(name = "device_id", length = 200, nullable = false)
    private String deviceId;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "last_seen", nullable = false)
    private LocalDateTime lastSeen;

    @Builder.Default
    @Column(name = "trusted", nullable = false)
    private boolean trusted = true;
}
