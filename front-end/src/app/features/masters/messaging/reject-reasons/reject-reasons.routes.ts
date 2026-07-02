import { Routes } from '@angular/router';

export const REJECTREASON_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/reject-reasons-list/reject-reasons-list.page').then((m) => m.RejectReasonListPage) },
    { path: 'create', loadComponent: () => import('./pages/reject-reasons-form/reject-reasons-form.page').then((m) => m.RejectReasonFormPage) },
    { path: ':id', loadComponent: () => import('./pages/reject-reasons-form/reject-reasons-form.page').then((m) => m.RejectReasonFormPage) }
];
