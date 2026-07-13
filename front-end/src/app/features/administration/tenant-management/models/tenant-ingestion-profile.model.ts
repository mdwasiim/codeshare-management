import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantIngestionChannel extends AuditableModel {
    id?: number;
    messageType?: string;
    sourceType?: string;
    enabled?: boolean;
    priority?: number;
    host?: string;
    port?: number;
    username?: string;
    remoteDirectory?: string;
    protocol?: string;
    brokerUrl?: string;
    queueName?: string;
    topicName?: string;
    fileIncludePattern?: string;
}

export interface TenantIngestionProfile extends AuditableModel {
    id?: number;
    tenantId?: number;
    tenantCode?: string;
    airlineCode?: string;
    sourceSystem: string;
    enabled?: boolean;
    pollIntervalMs?: number;
    channels?: TenantIngestionChannel[];
}
