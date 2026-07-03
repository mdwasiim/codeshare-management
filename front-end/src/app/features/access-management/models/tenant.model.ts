import { AuditableModel } from '@shared/models/auditable.model';

export interface Tenant extends AuditableModel {
    id?: string;

    name: string;
    code: string;
    description?: string;

    databaseConfigId?: string;

    plan?: string;
    subscriptionStart?: string;
    subscriptionEnd?: string;
    trial?: boolean;

    contactEmail?: string;
    contactPhone?: string;
    logoUrl?: string;
    region?: string;

    status?: TenantStatus;
}

export enum TenantStatus {
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    SUSPENDED = 'SUSPENDED',
    TRIAL = 'TRIAL',
    EXPIRED = 'EXPIRED'
}

export enum TenantPlan {
    BASIC = 'BASIC',
    PREMIUM = 'PREMIUM',
    ENTERPRISE = 'ENTERPRISE'
}

export const DEFAULT_TENANT: Tenant = {
    name: '',
    code: '',
    status: TenantStatus.ACTIVE,
    trial: false
};
