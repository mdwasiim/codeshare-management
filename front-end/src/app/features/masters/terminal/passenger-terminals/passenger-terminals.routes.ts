import { Routes } from '@angular/router';

export const PASSENGERTERMINAL_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/passenger-terminals-list/passenger-terminals-list.page').then((m) => m.PassengerTerminalListPage) },
    { path: 'create', loadComponent: () => import('./pages/passenger-terminals-form/passenger-terminals-form.page').then((m) => m.PassengerTerminalFormPage) },
    { path: ':id', loadComponent: () => import('./pages/passenger-terminals-form/passenger-terminals-form.page').then((m) => m.PassengerTerminalFormPage) }
];
