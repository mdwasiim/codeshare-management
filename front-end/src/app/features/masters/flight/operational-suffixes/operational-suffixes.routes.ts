import { Routes } from '@angular/router';

export const OPERATIONALSUFFIX_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/operational-suffixes-list/operational-suffixes-list.page').then((m) => m.OperationalSuffixListPage) },
    { path: 'create', loadComponent: () => import('./pages/operational-suffixes-form/operational-suffixes-form.page').then((m) => m.OperationalSuffixFormPage) },
    { path: ':id', loadComponent: () => import('./pages/operational-suffixes-form/operational-suffixes-form.page').then((m) => m.OperationalSuffixFormPage) }
];
