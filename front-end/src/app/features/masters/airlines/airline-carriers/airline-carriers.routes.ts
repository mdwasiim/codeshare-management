import { Routes } from '@angular/router';

export const AIRLINECARRIER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/airline-carriers-list/airline-carriers-list.page').then((m) => m.AirlineCarrierListPage) },
    { path: 'create', loadComponent: () => import('./pages/airline-carriers-form/airline-carriers-form.page').then((m) => m.AirlineCarrierFormPage) },
    { path: ':id', loadComponent: () => import('./pages/airline-carriers-form/airline-carriers-form.page').then((m) => m.AirlineCarrierFormPage) }
];
