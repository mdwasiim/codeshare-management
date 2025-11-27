package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

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
public class Permission {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;


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
