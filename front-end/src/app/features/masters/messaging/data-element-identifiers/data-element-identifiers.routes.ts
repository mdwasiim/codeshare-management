import { Routes } from '@angular/router';

export const DATAELEMENTIDENTIFIER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/data-element-identifiers-list/data-element-identifiers-list.page').then((m) => m.DataElementIdentifierListPage) },
    { path: 'create', loadComponent: () => import('./pages/data-element-identifiers-form/data-element-identifiers-form.page').then((m) => m.DataElementIdentifierFormPage) },
    { path: ':id', loadComponent: () => import('./pages/data-element-identifiers-form/data-element-identifiers-form.page').then((m) => m.DataElementIdentifierFormPage) }
];
