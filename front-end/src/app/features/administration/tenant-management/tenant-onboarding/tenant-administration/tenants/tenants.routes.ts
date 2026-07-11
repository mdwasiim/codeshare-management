import { Routes } from '@angular/router';

export const TENANTS_ROUTES: Routes = [
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
        loadComponent: () => import('./pages/tenant-form/tenant-form.page').then((m) => m.TenantFormPage)
    }
];
