import { AuditableModel } from '@shared/models/auditable.model';

export type TenantStatus = 'ACTIVE' | 'SUSPENDED' | 'EXPIRED' | 'DELETED';
export type TenantPlan = 'FREE' | 'PRO' | 'ENTERPRISE';

export enum AuthSource {
    INTERNAL = 'INTERNAL',
    LDAP = 'LDAP',
    AZURE = 'AZURE',
    KEYCLOAK = 'KEYCLOAK',
    OKTA = 'OKTA',
    OIDC_GENERIC = 'OIDC_GENERIC'
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
    authSource?: AuthSource;
    enabled?: boolean;
    priority?: number;
    providerId?: string;
    oidcConfig?: OidcConfig;
}

export interface TenantAuthContext {
    id?: number;
    name?: string;
    tenantCode?: string;
    status?: TenantStatus;
    logoUrl?: string;
    region?: string;
    identityProviders?: IdentityProviderConfig[];
}

export interface TenantLoginOption {
    id?: number;
    name: string;
    tenantCode: string;
    logoUrl?: string;
}

export interface Tenant extends AuditableModel {
    id?: number;
    name: string;
    tenantCode: string;
    description?: string;
    databaseConfigId?: number;
    plan?: TenantPlan;
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
    identityProviders?: IdentityProviderConfig[];
}
