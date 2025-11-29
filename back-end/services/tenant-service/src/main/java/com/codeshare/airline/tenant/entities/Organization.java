    package com.codeshare.airline.tenant.entities;

    import com.codeshare.airline.common.audit.AbstractEntity;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.HashSet;
    import java.util.Set;
    import java.util.UUID;

    @Entity
    @Table(name = "organizations")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Organization extends AbstractEntity {


        @Column(name = "name", nullable = false, length = 200)
        private String name;

        @Column(name = "code", nullable = false, unique = true, length = 100)
        private String code;

        @Column(name = "description", length = 500)
        private String description;

        @Column(name = "active", nullable = false)
        private boolean active = true;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "tenant_id", nullable = true)
        private Tenant tenant;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        private Organization parent;

        @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Organization> children = new HashSet<>();

        @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<UserGroup> userGroups = new HashSet<>();

    }
