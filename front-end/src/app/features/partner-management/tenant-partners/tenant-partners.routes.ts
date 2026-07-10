import { Routes } from '@angular/router';

export const TENANT_PARTNER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/tenant-partners-list/tenant-partners-list.page').then((m) => m.TenantPartnerListPage) },
    { path: 'create', loadComponent: () => import('./pages/tenant-partners-form/tenant-partners-form.page').then((m) => m.TenantPartnerFormPage) },
    { path: ':id', loadComponent: () => import('./pages/tenant-partners-form/tenant-partners-form.page').then((m) => m.TenantPartnerFormPage) }
];
