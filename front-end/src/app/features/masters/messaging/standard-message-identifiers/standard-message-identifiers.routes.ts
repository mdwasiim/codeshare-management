import { Routes } from '@angular/router';

export const STANDARDMESSAGEIDENTIFIER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/standard-message-identifiers-list/standard-message-identifiers-list.page').then((m) => m.StandardMessageIdentifierListPage) },
    { path: 'create', loadComponent: () => import('./pages/standard-message-identifiers-form/standard-message-identifiers-form.page').then((m) => m.StandardMessageIdentifierFormPage) },
    { path: ':id', loadComponent: () => import('./pages/standard-message-identifiers-form/standard-message-identifiers-form.page').then((m) => m.StandardMessageIdentifierFormPage) }
];
