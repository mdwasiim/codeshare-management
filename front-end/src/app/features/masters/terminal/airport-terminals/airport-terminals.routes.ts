import { Routes } from '@angular/router';

export const AIRPORTTERMINAL_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/airport-terminals-list/airport-terminals-list.page').then((m) => m.AirportTerminalListPage) },
    { path: 'create', loadComponent: () => import('./pages/airport-terminals-form/airport-terminals-form.page').then((m) => m.AirportTerminalFormPage) },
    { path: ':id', loadComponent: () => import('./pages/airport-terminals-form/airport-terminals-form.page').then((m) => m.AirportTerminalFormPage) }
];
