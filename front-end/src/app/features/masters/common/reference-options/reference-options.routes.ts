import { Routes } from '@angular/router';

export const COMMON_REFERENCE_OPTION_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/reference-options-list/reference-options-list.page').then((m) => m.CommonReferenceOptionListPage) },
    { path: 'create', loadComponent: () => import('./pages/reference-options-form/reference-options-form.page').then((m) => m.CommonReferenceOptionFormPage) },
    { path: ':id', loadComponent: () => import('./pages/reference-options-form/reference-options-form.page').then((m) => m.CommonReferenceOptionFormPage) }
];
