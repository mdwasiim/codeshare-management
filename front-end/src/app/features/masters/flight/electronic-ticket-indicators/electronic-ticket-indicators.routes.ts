import { Routes } from '@angular/router';

export const ELECTRONICTICKETINDICATOR_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/electronic-ticket-indicators-list/electronic-ticket-indicators-list.page').then((m) => m.ElectronicTicketIndicatorListPage) },
    { path: 'create', loadComponent: () => import('./pages/electronic-ticket-indicators-form/electronic-ticket-indicators-form.page').then((m) => m.ElectronicTicketIndicatorFormPage) },
    { path: ':id', loadComponent: () => import('./pages/electronic-ticket-indicators-form/electronic-ticket-indicators-form.page').then((m) => m.ElectronicTicketIndicatorFormPage) }
];
