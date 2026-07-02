import { Routes } from '@angular/router';

export const RESERVATIONBOOKINGDESIGNATOR_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/reservation-booking-designators-list/reservation-booking-designators-list.page').then((m) => m.ReservationBookingDesignatorListPage) },
    { path: 'create', loadComponent: () => import('./pages/reservation-booking-designators-form/reservation-booking-designators-form.page').then((m) => m.ReservationBookingDesignatorFormPage) },
    { path: ':id', loadComponent: () => import('./pages/reservation-booking-designators-form/reservation-booking-designators-form.page').then((m) => m.ReservationBookingDesignatorFormPage) }
];
