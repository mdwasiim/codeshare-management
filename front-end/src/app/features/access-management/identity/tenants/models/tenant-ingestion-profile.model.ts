import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantIngestionChannel extends AuditableModel {
    id?: string;
    messageType?: string;
    sourceType?: string;
    enabled?: boolean;
    priority?: number;
    host?: string;
    port?: number;
    username?: string;
    passwordEncrypted?: string;
    remoteDirectory?: string;
    protocol?: string;
    mailbox?: string;
    mailDelayMs?: number;
    mailUnseenOnly?: boolean;
    mailDelete?: boolean;
    brokerUrl?: string;
    queueName?: string;
    topicName?: string;
    fileIncludePattern?: string;
    fileExcludePattern?: string;
    filePollDelayMs?: number;
    fileMove?: string;
    fileMoveFailed?: string;
    connectionTimeoutMs?: number;
    readTimeoutMs?: number;
    retryAttempts?: number;
    retryDelayMs?: number;
}

export interface TenantIngestionProfile extends AuditableModel {
    id?: string;
    tenantId?: string;
    tenantCode?: string;
    airlineCode?: string;
    sourceSystem?: string;
    enabled?: boolean;
    pollIntervalMs?: number;
    channels?: TenantIngestionChannel[];
}
