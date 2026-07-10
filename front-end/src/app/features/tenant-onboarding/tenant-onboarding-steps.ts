export type TenantOnboardingStepKey =
    | 'tenant'
    | 'identity-providers'
    | 'oidc-config'
    | 'ingestion-profiles'
    | 'ingestion-channels'
    | 'codeshare-partners'
    | 'partner-profiles'
    | 'communication-profiles'
    | 'distribution-profiles'
    | 'review';

export type TenantOnboardingStepStatus = 'complete' | 'active' | 'pending';

export type TenantOnboardingStep = {
    key: TenantOnboardingStepKey;
    label: string;
    description: string;
    route: TenantOnboardingStepKey;
};

export const TENANT_ONBOARDING_STEPS: TenantOnboardingStep[] = [
    {
        key: 'tenant',
        label: 'Tenant',
        description: 'Host airline profile, subscription, and operating region',
        route: 'tenant'
    },
    {
        key: 'identity-providers',
        label: 'Identity Provider',
        description: 'Authentication source and provider priority',
        route: 'identity-providers'
    },
    {
        key: 'oidc-config',
        label: 'OIDC Config',
        description: 'Issuer, client, redirect, and endpoint configuration',
        route: 'oidc-config'
    },
    {
        key: 'ingestion-profiles',
        label: 'Ingestion Profile',
        description: 'Source system and polling behavior',
        route: 'ingestion-profiles'
    },
    {
        key: 'ingestion-channels',
        label: 'Ingestion Channels',
        description: 'Protocol-specific inbound transport settings',
        route: 'ingestion-channels'
    },
    {
        key: 'codeshare-partners',
        label: 'Codeshare Partner',
        description: 'Agreement setup for partner airlines',
        route: 'codeshare-partners'
    },
    {
        key: 'partner-profiles',
        label: 'Partner Profile',
        description: 'Operational partner rules and inventory settings',
        route: 'partner-profiles'
    },
    {
        key: 'communication-profiles',
        label: 'Communication',
        description: 'Transport, endpoint, and authentication profiles',
        route: 'communication-profiles'
    },
    {
        key: 'distribution-profiles',
        label: 'Distribution',
        description: 'Distribution mode and message delivery behavior',
        route: 'distribution-profiles'
    },
    {
        key: 'review',
        label: 'Review',
        description: 'Readiness check before activation',
        route: 'review'
    }
];
