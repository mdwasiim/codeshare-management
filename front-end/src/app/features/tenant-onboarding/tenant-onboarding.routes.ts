import { Routes } from '@angular/router';

export const TENANT_ONBOARDING_ROUTES: Routes = [
    {
        path: 'tenant-identity-providers',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Identity',
            title: 'Identity Providers',
            description: 'Select a tenant to configure authentication providers and OIDC settings.',
            actionLabel: 'Open Identity',
            targetStep: 'identity-providers'
        }
    },
    {
        path: 'tenant-oidc-config',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Identity',
            title: 'OIDC Configuration',
            description: 'Select a tenant to manage issuer, client, redirect, and token endpoint settings.',
            actionLabel: 'Open OIDC',
            targetStep: 'oidc-config'
        }
    },
    {
        path: 'tenant-ingestion-profiles',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Ingestion',
            title: 'Ingestion Profiles',
            description: 'Select a tenant to manage schedule ingestion profiles.',
            actionLabel: 'Open Ingestion',
            targetStep: 'ingestion-profiles'
        }
    },
    {
        path: 'tenant-ingestion-channels',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Ingestion',
            title: 'Ingestion Channels',
            description: 'Select a tenant to manage protocol-specific ingestion channels.',
            actionLabel: 'Open Channels',
            targetStep: 'ingestion-channels'
        }
    },
    {
        path: 'tenant-codeshare-partners',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Partners',
            title: 'Codeshare Partners',
            description: 'Select a tenant to manage partner agreements and registry records.',
            actionLabel: 'Open Partners',
            targetStep: 'codeshare-partners'
        }
    },
    {
        path: 'tenant-partner-profiles',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Partners',
            title: 'Partner Profiles',
            description: 'Select a tenant to manage partner profile configuration.',
            actionLabel: 'Open Partners',
            targetStep: 'partner-profiles'
        }
    },
    {
        path: 'tenant-communication-profiles',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Partners',
            title: 'Communication Profiles',
            description: 'Select a tenant to manage partner communication settings.',
            actionLabel: 'Open Communication',
            targetStep: 'communication-profiles'
        }
    },
    {
        path: 'tenant-distribution-profiles',
        loadComponent: () =>
            import('./pages/tenant-onboarding-launcher/tenant-onboarding-launcher.page').then((m) => m.TenantOnboardingLauncherPage),
        data: {
            eyebrow: 'Onboarding / Partners',
            title: 'Distribution Profiles',
            description: 'Select a tenant to manage distribution profile rules.',
            actionLabel: 'Open Distribution',
            targetStep: 'distribution-profiles'
        }
    },
    {
        path: 'tenant-onboarding/:id',
        loadComponent: () =>
            import('./pages/tenant-onboarding-workspace/tenant-onboarding-workspace.page').then((m) => m.TenantOnboardingWorkspacePage),
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'tenant'
            },
            {
                path: 'tenant',
                loadComponent: () =>
                    import('@features/access-management/identity/tenants/pages/tenant-detail/tabs/tenant-overview-tab.component').then((m) => m.TenantOverviewTabComponent)
            },
            {
                path: 'identity-providers',
                loadComponent: () =>
                    import('./identity/pages/tenant-identity-tab.component').then((m) => m.TenantIdentityTabComponent)
            },
            {
                path: 'oidc-config',
                loadComponent: () =>
                    import('./identity/pages/tenant-identity-tab.component').then((m) => m.TenantIdentityTabComponent)
            },
            {
                path: 'ingestion-profiles',
                loadComponent: () =>
                    import('./ingestion/pages/tenant-ingestion-tab.component').then((m) => m.TenantIngestionTabComponent)
            },
            {
                path: 'ingestion-channels',
                loadComponent: () =>
                    import('./ingestion/pages/tenant-ingestion-tab.component').then((m) => m.TenantIngestionTabComponent)
            },
            {
                path: 'codeshare-partners',
                loadComponent: () =>
                    import('./partners/pages/tenant-partners-tab.component').then((m) => m.TenantPartnersTabComponent)
            },
            {
                path: 'partner-profiles',
                loadComponent: () =>
                    import('./partners/pages/tenant-partners-tab.component').then((m) => m.TenantPartnersTabComponent)
            },
            {
                path: 'communication-profiles',
                loadComponent: () =>
                    import('./partners/pages/tenant-partners-tab.component').then((m) => m.TenantPartnersTabComponent)
            },
            {
                path: 'distribution-profiles',
                loadComponent: () =>
                    import('./partners/pages/tenant-partners-tab.component').then((m) => m.TenantPartnersTabComponent)
            },
            {
                path: 'review',
                loadComponent: () =>
                    import('./pages/tenant-onboarding-review/tenant-onboarding-review.page').then((m) => m.TenantOnboardingReviewPage)
            }
        ]
    }
];
