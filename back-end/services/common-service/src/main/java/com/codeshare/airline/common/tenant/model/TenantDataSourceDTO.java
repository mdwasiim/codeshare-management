package com.codeshare.airline.common.tenant.model;


import com.codeshare.airline.common.audit.AuditBaseDto;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDataSourceDTO extends AuditBaseDto {

    private UUID id;

    private String dbVendor;      // POSTGRES, MYSQL, ORACLE, MSSQL
    private String dbType;        // PRIMARY, REPLICA, ANALYTICS, ARCHIVE
    private String driverClass;   // org.postgresql.Driver
    private String dialect;       // org.hibernate.dialect.PostgreSQLDialect
    private String dbUrl;
    private String username;
    private String password;
    private String defaultPort;
    private String description;
    private Boolean active;

    private UUID tenantId;        // Foreign key reference to Tenant

}
