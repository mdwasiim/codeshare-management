import { Routes } from '@angular/router';

export const TENANT_IDENTITY_ROUTES: Routes = [
    {
        path: 'tenant-identity-providers',
        loadComponent: () => import('./pages/tenant-identity-providers/tenant-identity-providers.page').then((m) => m.TenantIdentityProvidersPage)
    },
    {
        path: 'tenant-oidc-config',
        loadComponent: () => import('./pages/tenant-oidc-config/tenant-oidc-config.page').then((m) => m.TenantOidcConfigPage)
    }
];
