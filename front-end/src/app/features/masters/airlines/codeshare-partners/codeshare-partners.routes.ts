import { Routes } from '@angular/router';

export const CODESHAREPARTNER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/codeshare-partners-list/codeshare-partners-list.page').then((m) => m.CodesharePartnerListPage) },
    { path: 'create', loadComponent: () => import('./pages/codeshare-partners-form/codeshare-partners-form.page').then((m) => m.CodesharePartnerFormPage) },
    { path: ':id', loadComponent: () => import('./pages/codeshare-partners-form/codeshare-partners-form.page').then((m) => m.CodesharePartnerFormPage) }
];
