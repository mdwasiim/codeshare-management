export interface Tenant {
    id?: string;

    // =========================
    // BASIC INFO
    // =========================
    name: string;
    code: string;
    description?: string;

    // =========================
    // CONFIG
    // =========================
    databaseConfigId?: string;

    // =========================
    // SUBSCRIPTION
    // =========================
    plan?: string; // later → enum
    subscriptionStart?: string;
    subscriptionEnd?: string;
    trial?: boolean;

    // =========================
    // CONTACT
    // =========================
    contactEmail?: string;
    contactPhone?: string;
    logoUrl?: string;
    region?: string;

    // =========================
    // STATUS
    // =========================
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
