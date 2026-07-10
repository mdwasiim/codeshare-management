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
    authSource?: AuthSource;
    oidcConfig?: OidcConfig;
}

export interface OidcConfig {
    issuerUri?: string;
    authorizationUri?: string;
    tokenUri?: string;
    jwkSetUri?: string;
    clientId?: string;
    clientSecretRef?: string;
    redirectUri?: string;
    scopes?: string;
    enforceRedirectUri?: boolean;
}

export interface IdentityProviderConfig {
    authSource: AuthSource;
    enabled: boolean;
    priority: number;
    providerId: string;
    oidcConfig?: OidcConfig;
}

export interface TenantAuthContext {
    id: string;
    name: string;
    tenantCode: string;
    status: TenantStatus;
    logoUrl?: string;
    region?: string;
    identityProviders: IdentityProviderConfig[];
}

export enum AuthSource {
    INTERNAL = 'INTERNAL',
    LDAP = 'LDAP',
    AZURE = 'AZURE',
    KEYCLOAK = 'KEYCLOAK',
    OKTA = 'OKTA',
    OIDC_GENERIC = 'OIDC_GENERIC'
}

export enum TenantStatus {
    ACTIVE = 'ACTIVE',
    SUSPENDED = 'SUSPENDED',
    EXPIRED = 'EXPIRED',
    DELETED = 'DELETED'
}

export enum TenantPlan {
    FREE = 'FREE',
    PRO = 'PRO',
    ENTERPRISE = 'ENTERPRISE'
}

export const DEFAULT_TENANT: Tenant = {
    name: '',
    code: '',
    authSource: AuthSource.INTERNAL,
    status: TenantStatus.ACTIVE,
    trial: false
};
