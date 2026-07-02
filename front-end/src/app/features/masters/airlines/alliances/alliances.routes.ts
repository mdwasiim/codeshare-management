import { Routes } from '@angular/router';

export const ALLIANCE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/alliances-list/alliances-list.page').then((m) => m.AllianceListPage) },
    { path: 'create', loadComponent: () => import('./pages/alliances-form/alliances-form.page').then((m) => m.AllianceFormPage) },
    { path: ':id', loadComponent: () => import('./pages/alliances-form/alliances-form.page').then((m) => m.AllianceFormPage) }
];
