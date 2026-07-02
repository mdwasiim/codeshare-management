import { Routes } from '@angular/router';

export const ALLIANCEMEMBER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/alliance-members-list/alliance-members-list.page').then((m) => m.AllianceMemberListPage) },
    { path: 'create', loadComponent: () => import('./pages/alliance-members-form/alliance-members-form.page').then((m) => m.AllianceMemberFormPage) },
    { path: ':id', loadComponent: () => import('./pages/alliance-members-form/alliance-members-form.page').then((m) => m.AllianceMemberFormPage) }
];
