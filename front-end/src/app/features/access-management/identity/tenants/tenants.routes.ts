import { Routes } from '@angular/router';

export const TENANT_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('./pages/tenant-list/tenant-list.page').then((m) => m.TenantListPage)
    },
    {
        path: 'create',
        loadComponent: () => import('./pages/tenant-form/tenant-form.page').then((m) => m.TenantFormPage)
    },
    {
        path: ':id',
        loadComponent: () => import('./pages/tenant-detail/tenant-detail.page').then((m) => m.TenantDetailPage),
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'overview'
            },
            {
                path: 'overview',
                loadComponent: () => import('./pages/tenant-detail/tabs/tenant-overview-tab.component').then((m) => m.TenantOverviewTabComponent)
            },
            {
                path: 'identity',
                loadComponent: () => import('./pages/tenant-detail/tabs/tenant-identity-tab.component').then((m) => m.TenantIdentityTabComponent)
            },
            {
                path: 'ingestion',
                loadComponent: () => import('./pages/tenant-detail/tabs/tenant-ingestion-tab.component').then((m) => m.TenantIngestionTabComponent)
            },
            {
                path: 'partners',
                loadComponent: () => import('./pages/tenant-detail/tabs/tenant-partners-tab.component').then((m) => m.TenantPartnersTabComponent)
            }
        ]
    },
    {
        path: ':id/edit',
        loadComponent: () => import('./pages/tenant-form/tenant-form.page').then((m) => m.TenantFormPage)
    }
];
