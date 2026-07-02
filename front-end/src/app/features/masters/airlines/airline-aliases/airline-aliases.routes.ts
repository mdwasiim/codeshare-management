import { Routes } from '@angular/router';

export const AIRLINEALIAS_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/airline-aliases-list/airline-aliases-list.page').then((m) => m.AirlineAliasListPage) },
    { path: 'create', loadComponent: () => import('./pages/airline-aliases-form/airline-aliases-form.page').then((m) => m.AirlineAliasFormPage) },
    { path: ':id', loadComponent: () => import('./pages/airline-aliases-form/airline-aliases-form.page').then((m) => m.AirlineAliasFormPage) }
];
