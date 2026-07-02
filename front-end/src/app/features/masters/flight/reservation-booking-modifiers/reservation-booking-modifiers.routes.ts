import { Routes } from '@angular/router';

export const RESERVATIONBOOKINGMODIFIER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/reservation-booking-modifiers-list/reservation-booking-modifiers-list.page').then((m) => m.ReservationBookingModifierListPage) },
    { path: 'create', loadComponent: () => import('./pages/reservation-booking-modifiers-form/reservation-booking-modifiers-form.page').then((m) => m.ReservationBookingModifierFormPage) },
    { path: ':id', loadComponent: () => import('./pages/reservation-booking-modifiers-form/reservation-booking-modifiers-form.page').then((m) => m.ReservationBookingModifierFormPage) }
];
