import { Routes } from '@angular/router';

export const AIRCRAFTOWNER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/owners-list/owners-list.page').then((m) => m.AircraftOwnerListPage) },
    { path: 'create', loadComponent: () => import('./pages/owners-form/owners-form.page').then((m) => m.AircraftOwnerFormPage) },
    { path: ':id', loadComponent: () => import('./pages/owners-form/owners-form.page').then((m) => m.AircraftOwnerFormPage) }
];
