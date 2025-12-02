package com.codeshare.airline.common.tenant.model;

import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDataSourceDTO extends AuditBaseDto {

    private UUID id;

    // Basic DB Info
    private String dbVendor;
    private String dbType;
    private String driverClass;
    private String dialect;

    // Connection Details
    private String dbUrl;
    private String username;
    private String password;
    private String host;
    private Integer port;
    private String databaseName;
    private String connectionParams;

    // Pooling Settings
    private String poolName;
    private Integer maxPoolSize;
    private Integer minIdle;
    private Integer connectionTimeout;
    private Integer idleTimeout;
    private Integer maxLifetime;

    // Security / SSL
    private Boolean sslEnabled;
    private String sslCertPath;

    // Metadata
    private String description;
    private Boolean active;

    private UUID tenantId;
}
