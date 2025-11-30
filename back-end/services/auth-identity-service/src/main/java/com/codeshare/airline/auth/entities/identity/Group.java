package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.auth.entities.authorization.UserGroupRole;
import com.codeshare.airline.common.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "auth_groups")  // 'group' is reserved
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group extends AbstractEntity {


    @Column(nullable = false, length = 200)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    // Correct — just store tenantId
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGroupRole> userGroupRoles = new HashSet<>();
}
