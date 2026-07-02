import { Routes } from '@angular/router';

export const AIRLINECONTACT_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/airline-contacts-list/airline-contacts-list.page').then((m) => m.AirlineContactListPage) },
    { path: 'create', loadComponent: () => import('./pages/airline-contacts-form/airline-contacts-form.page').then((m) => m.AirlineContactFormPage) },
    { path: ':id', loadComponent: () => import('./pages/airline-contacts-form/airline-contacts-form.page').then((m) => m.AirlineContactFormPage) }
];
