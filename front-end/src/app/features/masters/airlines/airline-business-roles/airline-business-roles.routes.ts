import { Routes } from '@angular/router';

export const AIRLINEBUSINESSROLE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/airline-business-roles-list/airline-business-roles-list.page').then((m) => m.AirlineBusinessRoleListPage) },
    { path: 'create', loadComponent: () => import('./pages/airline-business-roles-form/airline-business-roles-form.page').then((m) => m.AirlineBusinessRoleFormPage) },
    { path: ':id', loadComponent: () => import('./pages/airline-business-roles-form/airline-business-roles-form.page').then((m) => m.AirlineBusinessRoleFormPage) }
];
