import { Routes } from '@angular/router';

export const DEIREGISTRY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/dei-registry-list/dei-registry-list.page').then((m) => m.DeiRegistryListPage) },
    { path: 'create', loadComponent: () => import('./pages/dei-registry-form/dei-registry-form.page').then((m) => m.DeiRegistryFormPage) },
    { path: ':id', loadComponent: () => import('./pages/dei-registry-form/dei-registry-form.page').then((m) => m.DeiRegistryFormPage) }
];
