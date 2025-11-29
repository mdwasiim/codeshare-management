package com.codeshare.airline.auth.entities.authorization;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.common.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "group_role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "role_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRole extends AbstractEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
