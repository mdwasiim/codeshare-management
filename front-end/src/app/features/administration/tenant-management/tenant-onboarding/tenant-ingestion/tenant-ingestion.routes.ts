import { Routes } from '@angular/router';

export const TENANT_INGESTION_ROUTES: Routes = [
    {
        path: 'tenant-ingestion-profiles',
        loadComponent: () => import('./pages/tenant-ingestion-profiles/tenant-ingestion-profiles.page').then((m) => m.TenantIngestionProfilesPage)
    },
    {
        path: 'tenant-ingestion-channels',
        loadComponent: () => import('./pages/tenant-ingestion-channels/tenant-ingestion-channels.page').then((m) => m.TenantIngestionChannelsPage)
    }
];
