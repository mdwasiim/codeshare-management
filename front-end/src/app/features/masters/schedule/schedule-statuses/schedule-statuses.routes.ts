import { Routes } from '@angular/router';

export const SCHEDULESTATUS_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/schedule-statuses-list/schedule-statuses-list.page').then((m) => m.ScheduleStatusListPage) },
    { path: 'create', loadComponent: () => import('./pages/schedule-statuses-form/schedule-statuses-form.page').then((m) => m.ScheduleStatusFormPage) },
    { path: ':id', loadComponent: () => import('./pages/schedule-statuses-form/schedule-statuses-form.page').then((m) => m.ScheduleStatusFormPage) }
];
