package com.codeshare.airline.auth.entities.authorization;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "user_group_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_group_role",
                        columnNames = {"user_id", "group_id", "role_id"}
                )
        },
        indexes = {
                @Index(name = "idx_ugr_user", columnList = "user_id"),
                @Index(name = "idx_ugr_group", columnList = "group_id"),
                @Index(name = "idx_ugr_role", columnList = "role_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserGroupRole extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
