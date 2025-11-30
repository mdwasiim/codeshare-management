package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.common.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends AbstractEntity {


    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String code;


    @Column(length = 500)
    private String description;

    // Correct way — store only tenantId
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PermissionRole> permissionRoles = new HashSet<>();
}
