import { Routes } from '@angular/router';

export const TENANT_PARTNER_ROUTES: Routes = [
    {
        path: 'tenant-codeshare-partners',
        loadComponent: () => import('./pages/tenant-codeshare-partners/tenant-codeshare-partners.page').then((m) => m.TenantCodesharePartnersPage)
    },
    {
        path: 'tenant-partner-profiles',
        loadComponent: () => import('./pages/tenant-partner-profiles/tenant-partner-profiles.page').then((m) => m.TenantPartnerProfilesPage)
    },
    {
        path: 'tenant-communication-profiles',
        loadComponent: () => import('./pages/tenant-communication-profiles/tenant-communication-profiles.page').then((m) => m.TenantCommunicationProfilesPage)
    },
    {
        path: 'tenant-distribution-profiles',
        loadComponent: () => import('./pages/tenant-distribution-profiles/tenant-distribution-profiles.page').then((m) => m.TenantDistributionProfilesPage)
    }
];
