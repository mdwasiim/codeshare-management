import { Routes } from '@angular/router';

export const ACTIONIDENTIFIER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/action-identifiers-list/action-identifiers-list.page').then((m) => m.ActionIdentifierListPage) },
    { path: 'create', loadComponent: () => import('./pages/action-identifiers-form/action-identifiers-form.page').then((m) => m.ActionIdentifierFormPage) },
    { path: ':id', loadComponent: () => import('./pages/action-identifiers-form/action-identifiers-form.page').then((m) => m.ActionIdentifierFormPage) }
];
