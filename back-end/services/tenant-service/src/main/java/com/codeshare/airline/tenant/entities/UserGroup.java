package com.codeshare.airline.tenant.entities;


import com.codeshare.airline.common.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroup extends AbstractEntity {


    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String code;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false)
    private Boolean active = true;

}
